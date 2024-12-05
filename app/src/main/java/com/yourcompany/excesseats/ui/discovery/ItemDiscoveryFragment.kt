package com.yourcompany.excesseats.ui.discovery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.databinding.FragmentItemDiscoveryBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.content.Context
import android.location.LocationManager
import android.content.Intent
import android.provider.Settings
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient

private const val TAG = "ItemDiscoveryFragment"

class ItemDiscoveryFragment : Fragment() {

    private var _binding: FragmentItemDiscoveryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: FoodPostAdapter
    private var googleMap: GoogleMap? = null
    private var currentLocation: LatLng? = null
    private var isLoading = false
    private val repository = FoodPostRepository.getInstance()
    private val foodPosts = mutableListOf<FoodPost>()
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        private const val SEARCH_RADIUS_KM = 3000.0
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
        setupRecyclerView()
        setupSearch()
        setupMap(savedInstanceState)
        loadPosts() // Load all posts immediately
        checkLocationPermission() // Then try to get location for filtering
    }

    private fun setupRecyclerView() {
        adapter = FoodPostAdapter(
            onItemClick = { foodPost ->
                findNavController().navigate(
                    ItemDiscoveryFragmentDirections.actionItemDiscoveryFragmentToFoodPostDetailFragment(
                        postId = foodPost.id,
                        title = foodPost.title,
                        latitude = foodPost.latitude.toFloat(),
                        longitude = foodPost.longitude.toFloat()
                    )
                )
            },
            userLocation = currentLocation
        )
        binding.foodPostsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            adapter = this@ItemDiscoveryFragment.adapter
        }

        // Load initial posts
        loadPosts()
    }

    private fun setupSearch() {
        binding.searchInput.setOnEditorActionListener { textView, _, _ ->
            filterPosts(textView.text.toString())
            true
        }
    }

    private fun setupMap(savedInstanceState: Bundle?) {
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync { map ->
            googleMap = map
            updateMapLocation(currentLocation)
        }
    }

    private fun checkLocationPermission() {
        Log.d(TAG, "Checking location permission")
        
        if (!isLocationEnabled()) {
            Log.d(TAG, "Location is disabled")
            Toast.makeText(
                requireContext(),
                "Please enable location services",
                Toast.LENGTH_LONG
            ).show()
            // Open location settings
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Location permission already granted")
            getCurrentLocation()
        } else {
            Log.d(TAG, "Requesting location permission")
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocation() {
        Log.d(TAG, "Getting current location")
        try {
            val locationRequest = LocationRequest.Builder(10000) // 10 seconds interval
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        Log.d(TAG, "Got real-time location: ${location.latitude}, ${location.longitude}")
                        currentLocation = LatLng(location.latitude, location.longitude)
                        updateMapLocation(currentLocation)
                        adapter.userLocation = currentLocation
                        loadPosts()
                        // Remove updates after getting location
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }
            }, Looper.getMainLooper())

            // Also try getting last known location as backup
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    Log.d(TAG, "Got last known location: ${location.latitude}, ${location.longitude}")
                    currentLocation = LatLng(location.latitude, location.longitude)
                    updateMapLocation(currentLocation)
                    adapter.userLocation = currentLocation
                    loadPosts()
                }
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception getting location", e)
            Toast.makeText(
                requireContext(),
                "Location permission is required to show nearby food posts",
                Toast.LENGTH_LONG
            ).show()
            loadPosts()
        }
    }

    private fun updateMapLocation(location: LatLng?) {
        location?.let {
            googleMap?.apply {
                clear()
                moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12f))
                addMarker(
                    MarkerOptions()
                        .position(it)
                        .title("Your Location")
                )
                updateMapWithPosts()
            }
        }
    }

    private fun updateMapWithPosts() {
        googleMap?.apply {
            val userMarker = currentLocation?.let {
                MarkerOptions()
                    .position(it)
                    .title("Your Location")
            }

            clear() // Clear existing markers
            userMarker?.let { addMarker(it) } // Add back user marker if exists

            foodPosts.forEach { post ->
                Log.d(TAG, "Adding marker for post: ${post.title} at ${post.latitude}, ${post.longitude}")
                val postLocation = LatLng(post.latitude, post.longitude)
                addMarker(
                    MarkerOptions()
                        .position(postLocation)
                        .title(post.title)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )
            }
        }
    }

    private fun loadPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            isLoading = true
            Log.d(TAG, "Loading posts. Current location: $currentLocation")
            
            if (currentLocation != null) {
                Log.d(TAG, "Loading nearby posts within ${SEARCH_RADIUS_KM}km")
                repository.getNearbyPosts(currentLocation!!, radiusKm = SEARCH_RADIUS_KM)
                    .collectLatest { result: Result<List<FoodPost>> ->
                        Log.d(TAG, "Received nearby posts result: $result")
                        handlePostsResult(result)
                    }
            } else {
                Log.d(TAG, "Loading all posts (no location available)")
                repository.getAllPosts()
                    .collectLatest { result: Result<List<FoodPost>> ->
                        Log.d(TAG, "Received all posts result: $result")
                        handlePostsResult(result)
                    }
            }
            isLoading = false
        }
    }

    private fun handlePostsResult(result: Result<List<FoodPost>>) {
        result.onSuccess { posts ->
            Log.d(TAG, "Loaded ${posts.size} posts")
            posts.forEach { post ->
                Log.d(TAG, "Post: ${post.title} at ${post.latitude}, ${post.longitude}")
            }

            // Sort posts by distance if location is available
            val sortedPosts = if (currentLocation != null) {
                posts.sortedBy { post ->
                    calculateDistance(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude,
                        post.latitude,
                        post.longitude
                    )
                }
            } else {
                posts
            }

            foodPosts.clear()
            foodPosts.addAll(sortedPosts)
            adapter.submitList(sortedPosts)
            updateMapWithPosts()
        }.onFailure { exception ->
            Log.e(TAG, "Failed to load posts", exception)
            Toast.makeText(
                requireContext(),
                "Failed to load posts: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return r * c
    }

    private fun filterPosts(query: String) {
        val filteredPosts = foodPosts.filter { post ->
            post.title.contains(query, ignoreCase = true) ||
            post.description.contains(query, ignoreCase = true) ||
            post.foodType.contains(query, ignoreCase = true)
        }
        adapter.submitList(filteredPosts)
        updateMapWithPosts()
    }

    override fun onResume() {
        super.onResume()
        _binding?.mapView?.onResume()
        
        // Recheck location when returning to the fragment
        if (isLocationEnabled() && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        _binding?.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        _binding?.mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        _binding?.mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding?.mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Location permission granted")
                    getCurrentLocation()
                } else {
                    Log.w(TAG, "Location permission denied")
                    Toast.makeText(
                        requireContext(),
                        "Location permission is required to show nearby food posts",
                        Toast.LENGTH_LONG
                    ).show()
                    loadPosts() // Load all posts without location filtering
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}
