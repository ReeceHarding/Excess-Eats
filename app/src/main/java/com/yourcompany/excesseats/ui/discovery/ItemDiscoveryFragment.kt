package com.yourcompany.excesseats.ui.discovery

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.chip.Chip
import com.google.android.material.slider.Slider
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.databinding.FragmentItemDiscoveryBinding
import com.yourcompany.excesseats.ui.discovery.adapters.FoodPostAdapter
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

class ItemDiscoveryFragment : Fragment() {
    private var _binding: FragmentItemDiscoveryBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private val foodPosts = mutableListOf<FoodPost>()
    private val selectedFoodTypes = mutableSetOf<String>()
    private val foodPostRepository = FoodPostRepository.getInstance()
    private var currentLocation: LatLng? = null
    private var maxDistance: Int = 25 // Default 25km
    private var userMarker: Marker? = null

    private val foodPostAdapter = FoodPostAdapter { post ->
        val action = ItemDiscoveryFragmentDirections.actionItemDiscoveryFragmentToFoodPostDetailFragment(
            postId = post.id,
            title = post.title,
            latitude = post.latitude.toFloat(),
            longitude = post.longitude.toFloat()
        )
        findNavController().navigate(action)
    }

    private val foodTypes = arrayOf(
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemDiscoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupMap(savedInstanceState)
        setupLocationUpdates()
        observePosts()
    }

    private fun setupViews() {
        // Setup RecyclerView
        binding.foodPostsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodPostAdapter
        }

        // Setup search
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
        })

        // Setup distance slider
        binding.distanceSlider.apply {
            value = maxDistance.toFloat()
            addOnChangeListener { _, value, _ ->
                maxDistance = value.toInt()
                binding.distanceText.text = "$maxDistance km"
                applyFilters()
            }
        }

        // Setup food type filters
        setupFoodTypeFilters()
    }

    private fun setupFoodTypeFilters() {
        binding.foodTypeChipGroup.apply {
            removeAllViews() // Clear existing chips
            foodTypes.forEach { foodType ->
                addView(createChip(foodType))
            }
        }
    }

    private fun createChip(foodType: String): Chip {
        return Chip(requireContext()).apply {
            text = foodType
            isCheckable = true
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedFoodTypes.add(foodType)
                } else {
                    selectedFoodTypes.remove(foodType)
                }
                applyFilters()
            }
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            googleMap = map
            checkLocationPermission()
        }
    }

    private fun setupLocationUpdates() {
        if (checkLocationPermission()) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        currentLocation = LatLng(it.latitude, it.longitude)
                        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 12f))
                        observeNearbyPosts(currentLocation!!)
                    }
                }
            } catch (e: SecurityException) {
                Toast.makeText(context, "Error getting location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun observePosts() {
        lifecycleScope.launch {
            foodPostRepository.getAllPosts().collect { result ->
                handlePostsResult(result)
            }
        }
    }

    private fun observeNearbyPosts(location: LatLng) {
        lifecycleScope.launch {
            foodPostRepository.getNearbyPosts(location, maxDistance.toDouble()).collect { result ->
                handlePostsResult(result)
            }
        }
    }

    private fun handlePostsResult(result: Result<List<FoodPost>>) {
        result.onSuccess { posts ->
            foodPosts.clear()
            foodPosts.addAll(posts)
            applyFilters()
        }.onFailure { error ->
            Toast.makeText(
                requireContext(),
                "Error loading posts: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun applyFilters() {
        val filteredPosts = getFilteredPosts()
        foodPostAdapter.submitList(filteredPosts)
        updateMapWithPosts()
    }

    private fun getFilteredPosts(): List<FoodPost> {
        val query = binding.searchInput.text.toString()
        var filteredPosts = if (query.isEmpty() && selectedFoodTypes.isEmpty()) {
            foodPosts
        } else {
            foodPosts.filter { post ->
                val matchesSearch = query.isEmpty() || (
                    post.title.contains(query, ignoreCase = true) ||
                    post.description.contains(query, ignoreCase = true) ||
                    post.foodType.contains(query, ignoreCase = true) ||
                    post.location.contains(query, ignoreCase = true) ||
                    post.quantity.contains(query, ignoreCase = true)
                )

                val matchesFoodType = selectedFoodTypes.isEmpty() || selectedFoodTypes.contains(post.foodType)

                matchesSearch && matchesFoodType
            }
        }

        // Apply distance filter if location is available
        currentLocation?.let { location ->
            filteredPosts = filteredPosts.filter { post ->
                calculateDistance(
                    location.latitude, location.longitude,
                    post.latitude, post.longitude
                ) <= maxDistance
            }
        }

        return filteredPosts
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    private fun updateMapWithPosts() {
        googleMap?.clear()

        // Add user location marker (red)
        currentLocation?.let { location ->
            userMarker = googleMap?.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
        }

        // Add food post markers (green)
        getFilteredPosts().forEach { post ->
            googleMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(post.latitude, post.longitude))
                    .title(post.title)
                    .snippet("${post.getRemainingServings()} servings left")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}
