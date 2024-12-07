package com.yourcompany.excesseats.ui.discovery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.chip.Chip
import com.yourcompany.excesseats.R
import com.yourcompany.excesseats.data.model.FoodCategory
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.databinding.FragmentFoodPostDetailBinding
import com.yourcompany.excesseats.utils.Logger
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FoodPostDetailFragment : Fragment() {

    private var _binding: FragmentFoodPostDetailBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private val args: FoodPostDetailFragmentArgs by navArgs()
    private val foodPostRepository = FoodPostRepository.getInstance()
    private val userRepository = UserRepository.getInstance()
    private var isClaimed = false

    companion object {
        private const val TAG = "FoodPostDetailFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupMap(savedInstanceState)
        loadFoodPost()
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
            updateMapLocation()
        }
    }

    private fun loadFoodPost() {
        lifecycleScope.launch {
            foodPostRepository.getFoodPost(args.postId).onSuccess { post ->
                updateUI(post)
            }.onFailure { exception ->
                Toast.makeText(
                    requireContext(),
                    "Error loading food post: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateUI(post: FoodPost) {
        try {
            val currentUser = userRepository.getCurrentUser()
            binding.apply {
                titleText.text = post.title
                foodTypeChip.text = post.foodType
                locationText.text = post.location
                timeText.text = if (post.pickupTime != 0L) {
                    "Available until ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(post.pickupTime))}"
                } else ""

                // Always show remaining servings
                val remainingServings = post.getRemainingServings()
                quantityText.text = "$remainingServings left of ${post.quantity}"

                // Fetch user's name
                lifecycleScope.launch {
                    userRepository.getUser(post.userId).onSuccess { user ->
                        postedByText.text = "Posted by: ${user.name}"
                    }.onFailure { exception ->
                        postedByText.text = "Posted by: Anonymous"
                        Logger.e("Error fetching user", exception)
                    }
                }

                containerText.text = if (post.containersAvailable) "Containers available" else "Bring your own container"
                descriptionText.text = post.description

                // Update claim button state
                val hasUserClaimed = currentUser?.let { post.isClaimedByUser(it.uid) } ?: false
                claimButton.isEnabled = !hasUserClaimed && remainingServings > 0
                claimButton.text = when {
                    hasUserClaimed -> "You've already claimed this"
                    remainingServings <= 0 -> "No servings left"
                    else -> "Claim Food"
                }

                if (!post.imageUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(post.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .centerCrop()
                        .into(foodImageView)
                }
            }
            updateMapLocation()
        } catch (e: Exception) {
            Logger.e("Error updating UI", e)
            Toast.makeText(context, "Error loading post details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapLocation() {
        try {
            googleMap?.let { map ->
                val location = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
                map.clear()
                map.addMarker(MarkerOptions().position(location).title(args.title))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating map", e)
        }
    }

    private fun setupViews() {
        setupToolbar()
        binding.claimButton.setOnClickListener {
            claimFood()
        }
    }

    private fun setupFoodTypeChip() {
        binding.foodTypeChip.apply {
            isCheckable = false
            isClickable = false
        }
    }

    private fun setupImageView() {
        binding.foodImageView.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setOnClickListener {
                // TODO: Implement image zoom/preview
            }
        }
    }

    private fun setupButtons() {
        binding.claimButton.setOnClickListener {
            if (!isClaimed) {
                claimFood()
            }
        }

        binding.directionsButton.setOnClickListener {
            // Open Google Maps with directions
            val uri = Uri.parse("google.navigation:q=${args.latitude},${args.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.google.android.apps.maps")
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Google Maps app is not installed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun claimFood() {
        val currentUser = userRepository.getCurrentUser()
        if (currentUser == null) {
            Toast.makeText(context, "Please log in to claim food", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val result = foodPostRepository.claimPost(args.postId, currentUser.uid)
                when {
                    result.isSuccess -> {
                        isClaimed = true
                        binding.claimButton.text = "Claimed"
                        binding.claimButton.isEnabled = false
                        Toast.makeText(context, "Successfully claimed food!", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    }
                    result.isFailure -> {
                        val error = result.exceptionOrNull()
                        Toast.makeText(context, "Error: ${error?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }
}
