package com.yourcompany.excesseats.ui.posting

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.yourcompany.excesseats.R
import com.yourcompany.excesseats.auth.LoginActivity
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.databinding.FragmentItemPostingBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ItemPostingFragment : Fragment() {

    private var _binding: FragmentItemPostingBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null
    private var selectedLocation: LatLng? = null
    private var selectedAddress: String? = null
    private var selectedTime: Date? = null
    private var selectedImageUri: Uri? = null
    private var currentPhotoPath: String? = null

    private val repository = FoodPostRepository.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(requireContext(), getString(R.string.permission_denied_camera), Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPhotoPath?.let { path ->
                val photoFile = File(path)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.yourcompany.excesseats.fileprovider",
                    photoFile
                )
                selectedImageUri = photoUri
                showImagePreview(photoUri)
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    Log.d("ItemPostingFragment", "Document selected: $uri")
                    // Take persistable URI permission
                    val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    requireContext().contentResolver.takePersistableUriPermission(uri, takeFlags)
                    selectedImageUri = uri
                    showImagePreview(uri)
                } catch (e: Exception) {
                    Log.e("ItemPostingFragment", "Error handling selected document", e)
                    Toast.makeText(requireContext(), "Error handling selected document: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Log.d("ItemPostingFragment", "No document selected")
            }
        } else {
            Log.d("ItemPostingFragment", "Document selection cancelled or failed")
        }
    }

    private val foodCategories = arrayOf(
        "American",
        "Asian Fusion",
        "Bakery",
        "Barbecue",
        "Breakfast",
        "Chinese",
        "Desserts",
        "Indian",
        "Italian",
        "Japanese",
        "Korean",
        "Mediterranean",
        "Mexican",
        "Middle Eastern",
        "Pizza",
        "Sandwiches",
        "Seafood",
        "Thai",
        "Vegetarian",
        "Other"
    )

    private var debugModeEnabled: Boolean = false

    private val debugTag = "ItemPostingFragmentDebug"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        debugModeEnabled = requireActivity().intent.getBooleanExtra("DEBUG_MODE", false)
        if (debugModeEnabled) {
            Log.d(debugTag, "Debug mode is enabled.")
        }

        setupToolbar()
        setupMap(savedInstanceState)
        setupLocationAutocomplete()
        setupTimeSelection()
        setupFoodTypeDropdown()
        setupImageUpload()
        setupSubmitButton()

        if (debugModeEnabled) {
            logDebugInformation()
        }
    }

    private fun logDebugInformation() {
        Log.d(debugTag, "User authenticated: ${auth.currentUser != null}")
        Log.d(debugTag, "Default food categories: ${foodCategories.joinToString(", ")}")
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            googleMap = map
            selectedLocation?.let { location ->
                updateMapLocation(location)
            }
        }
    }

    private fun setupLocationAutocomplete() {
        try {
            // Initialize Places if it hasn't been initialized yet
            if (!Places.isInitialized()) {
                Places.initialize(requireContext(), getString(R.string.google_maps_key))
            }

            // Get the autocomplete fragment
            val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as? AutocompleteSupportFragment
                    ?: return

            // Specify the types of place data to return
            autocompleteFragment.setPlaceFields(listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            ))

            // Set hint text
            autocompleteFragment.setHint(getString(R.string.hint_location))

            // Set up the place selection listener
            autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    selectedLocation = place.latLng
                    selectedAddress = place.address
                    selectedLocation?.let { updateMapLocation(it) }
                }

                override fun onError(status: com.google.android.gms.common.api.Status) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_place_selection, status.statusMessage),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                getString(R.string.error_location_setup),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupTimeSelection() {
        binding.pickupTimeInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCalendar.set(Calendar.MINUTE, minute)

                    // If selected time is in the past, add 24 hours
                    if (selectedCalendar.before(calendar)) {
                        selectedCalendar.add(Calendar.DAY_OF_MONTH, 1)
                    }

                    selectedTime = selectedCalendar.time
                    val timeFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())
                    binding.pickupTimeInput.setText(timeFormat.format(selectedTime ?: Calendar.getInstance().time))
                },
                currentHour,
                currentMinute,
                false
            ).show()
        }
    }

    private fun setupFoodTypeDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            foodCategories
        )
        binding.foodTypeInput.setAdapter(adapter)
        binding.foodTypeInput.threshold = 1  // Show suggestions after 1 character
    }

    private fun setupImageUpload() {
        binding.uploadImageButton.setOnClickListener {
            showImagePickerDialog()
        }
    }

    private fun showImagePickerDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_image_source))
            .setItems(arrayOf(getString(R.string.camera), getString(R.string.gallery))) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndLaunch()
                    1 -> launchGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
                showCameraPermissionRationale()
            }
            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }
    }

    private fun showCameraPermissionRationale() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.camera_permission_title))
            .setMessage(getString(R.string.camera_permission_rationale))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        val photoURI = FileProvider.getUriForFile(
            requireContext(),
            "com.yourcompany.excesseats.fileprovider",
            photoFile
        )
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        takePictureLauncher.launch(takePictureIntent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().cacheDir // Use cache directory instead of external storage
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun launchGallery() {
        try {
            Log.d("ItemPostingFragment", "Launching document picker")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
            pickImageLauncher.launch(intent)
        } catch (e: Exception) {
            Log.e("ItemPostingFragment", "Error launching document picker", e)
            Toast.makeText(requireContext(), "Error opening document picker: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImagePreview(uri: Uri) {
        binding.imagePreview.visibility = View.VISIBLE
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.imagePreview)
    }

    private suspend fun uploadImageToFirebase(uri: Uri): String {
        Log.d("ItemPostingFragment", "Starting image upload for URI: $uri")
        try {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

            // Get input stream from content resolver with try-with-resources
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                Log.d("ItemPostingFragment", "Reading image data from URI")
                val bytes = inputStream.readBytes()
                Log.d("ItemPostingFragment", "Successfully read ${bytes.size} bytes")

                if (bytes.isEmpty()) {
                    throw Exception("Image data is empty")
                }

                Log.d("ItemPostingFragment", "Starting upload to Firebase Storage")
                // Create metadata to set content type
                val metadata = com.google.firebase.storage.StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build()

                val uploadTask = imageRef.putBytes(bytes, metadata).await()
                Log.d("ItemPostingFragment", "Upload completed, bytes transferred: ${uploadTask.bytesTransferred}")

                if (uploadTask.bytesTransferred > 0) {
                    val downloadUrl = imageRef.downloadUrl.await().toString()
                    Log.d("ItemPostingFragment", "Got download URL: $downloadUrl")
                    return downloadUrl
                } else {
                    throw Exception("No bytes were transferred during upload")
                }
            } ?: throw Exception("Failed to read image data")
        } catch (e: Exception) {
            Log.e("ItemPostingFragment", "Error during image upload", e)
            throw e
        }
    }

    private fun setupSubmitButton() {
        binding.submitButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                // User is not logged in, redirect to login
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                return@setOnClickListener
            }

            // Show loading state
            setLoadingState(true)

            val title = binding.titleInput.text.toString()
            val foodType = binding.foodTypeInput.text.toString()
            val quantity = binding.quantityInput.text.toString()
            val description = binding.descriptionInput.text.toString()

            if (validateInput(title, foodType, quantity, description)) {
                // Ensure we have a future pickup time
                val pickupTime = selectedTime ?: run {
                    val futureTime = Calendar.getInstance()
                    futureTime.add(Calendar.HOUR_OF_DAY, 1) // Default to 1 hour from now
                    futureTime.time
                }

                // Upload image to Firebase Storage if selected
                lifecycleScope.launch {
                    try {
                        val imageUrl = selectedImageUri?.let { uri ->
                            uploadImageToFirebase(uri)
                        }

                        Log.d("ItemPostingFragment", "Final imageUrl for FoodPost: $imageUrl")

                        val foodPost = FoodPost(
                            id = UUID.randomUUID().toString(),
                            title = title,
                            description = description,
                            foodType = foodType,
                            quantity = quantity,
                            remainingQuantity = quantity.split(" ")[0].toIntOrNull() ?: 1,
                            location = selectedAddress ?: "",
                            latitude = selectedLocation?.latitude ?: 0.0,
                            longitude = selectedLocation?.longitude ?: 0.0,
                            pickupTime = pickupTime.time,
                            containersAvailable = binding.containerSwitch.isChecked,
                            imageUrl = imageUrl ?: "",
                            userId = currentUser.uid
                        )

                        // Save the post
                        val result = repository.createPost(foodPost)
                        result.onSuccess {
                            Toast.makeText(requireContext(), "Food post created successfully!", Toast.LENGTH_SHORT).show()
                            setLoadingState(false)  // Reset loading state before navigation
                            findNavController().navigateUp()
                        }.onFailure { exception ->
                            setLoadingState(false)  // Reset loading state on failure
                            Toast.makeText(requireContext(), "Failed to create post: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        setLoadingState(false)  // Reset loading state on exception
                        Toast.makeText(requireContext(), "Failed to upload image: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                setLoadingState(false)  // Reset loading state if validation fails
            }
        }
    }

    // Add this helper function to manage loading state
    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            submitButton.isEnabled = !isLoading
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            // Optionally disable other input fields while loading
            titleInput.isEnabled = !isLoading
            foodTypeInput.isEnabled = !isLoading
            quantityInput.isEnabled = !isLoading
            descriptionInput.isEnabled = !isLoading
            uploadImageButton.isEnabled = !isLoading
            containerSwitch.isEnabled = !isLoading
        }
    }

    private fun updateMapLocation(location: LatLng) {
        googleMap?.apply {
            clear()
            addMarker(MarkerOptions().position(location))
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    private fun validateInput(
        title: String,
        foodType: String,
        quantity: String,
        description: String
    ): Boolean {
        var isValid = true

        if (title.isBlank()) {
            binding.titleLayout.error = "Title is required"
            isValid = false
        }

        if (foodType.isBlank()) {
            binding.foodTypeLayout.error = "Food type is required"
            isValid = false
        }

        if (quantity.isBlank()) {
            binding.quantityLayout.error = "Quantity is required"
            isValid = false
        }

        if (description.isBlank()) {
            binding.descriptionLayout.error = "Description is required"
            isValid = false
        }

        if (selectedLocation == null) {
            binding.locationLayout.error = "Location is required"
            isValid = false
        }

        // Check if pickup time is selected and in the future
        val now = Calendar.getInstance().time
        if (selectedTime == null) {
            binding.pickupTimeLayout.error = "Pickup time is required"
            isValid = false
        } else if (selectedTime!!.before(now)) {
            binding.pickupTimeLayout.error = "Pickup time must be in the future"
            isValid = false
        }

        return isValid
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // No need to cleanup mapView here as it's handled in onDestroyView
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }
}
