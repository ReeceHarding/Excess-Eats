package com.yourcompany.excesseats.ui.discovery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
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
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.databinding.FragmentItemDiscoveryBinding
import com.yourcompany.excesseats.ui.discovery.adapters.FoodPostAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ItemDiscoveryFragment : Fragment() {
    private var _binding: FragmentItemDiscoveryBinding? = null
    private val binding get() = _binding!!

    private lateinit var foodPostAdapter: FoodPostAdapter
    private var googleMap: GoogleMap? = null
    private var currentLocation: LatLng? = null
    private var isLoading = false
    private val repository = FoodPostRepository.getInstance()
    private val foodPosts = mutableListOf<FoodPost>()
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    private var locationCallback: LocationCallback? = null

    companion object {
        private const val TAG = "ItemDiscoveryFragment"
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
        checkLocationPermission()
    }

    private fun setupRecyclerView() {
        foodPostAdapter = FoodPostAdapter(
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
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = foodPostAdapter
        }
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
        if (!isLocationEnabled()) {
            Toast.makeText(
                requireContext(),
                "Please enable location services",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            return
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getCurrentLocation() {
        try {
            val locationRequest = LocationRequest.Builder(10000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let { location ->
                        if (view != null && isAdded) {
                            currentLocation = LatLng(location.latitude, location.longitude)
                            updateMapLocation(currentLocation)
                            foodPostAdapter.userLocation = currentLocation
                            loadPosts()
                            removeLocationUpdates()
                        }
                    }
                }
            }

            locationCallback?.let { callback ->
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    callback,
                    Looper.getMainLooper()
                )
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null && view != null && isAdded) {
                    currentLocation = LatLng(location.latitude, location.longitude)
                    updateMapLocation(currentLocation)
                    foodPostAdapter.userLocation = currentLocation
                    loadPosts()
                }
            }
        } catch (e: SecurityException) {
            if (view != null && isAdded) {
                Toast.makeText(
                    requireContext(),
                    "Location permission is required to show nearby food posts",
                    Toast.LENGTH_LONG
                ).show()
                loadPosts()
            }
        }
    }

    private fun removeLocationUpdates() {
        locationCallback?.let { callback ->
            fusedLocationClient.removeLocationUpdates(callback)
        }
        locationCallback = null
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

            clear()
            userMarker?.let { addMarker(it) }

            foodPosts.forEach { post ->
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
        if (view == null || !isAdded) return

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                isLoading = true
                if (currentLocation != null) {
                    repository.getNearbyPosts(currentLocation!!, radiusKm = SEARCH_RADIUS_KM)
                        .collectLatest { result ->
                            if (view != null && isAdded) {
                                handlePostsResult(result)
                            }
                        }
                } else {
                    repository.getAllPosts()
                        .collectLatest { result ->
                            if (view != null && isAdded) {
                                handlePostsResult(result)
                            }
                        }
                }
            } finally {
                isLoading = false
            }
        }
    }

    private fun handlePostsResult(result: Result<List<FoodPost>>) {
        result.onSuccess { posts ->
            foodPosts.clear()
            foodPosts.addAll(posts)
            foodPostAdapter.submitList(posts)
            updateMapWithPosts()
        }.onFailure { error ->
            Toast.makeText(
                requireContext(),
                "Error loading posts: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun filterPosts(query: String) {
        val filteredPosts = if (query.isEmpty()) {
            foodPosts
        } else {
            foodPosts.filter { post ->
                post.title.contains(query, ignoreCase = true) ||
                        post.description.contains(query, ignoreCase = true) ||
                        post.foodType.contains(query, ignoreCase = true)
            }
        }
        foodPostAdapter.submitList(filteredPosts)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location permission denied. Some features may be limited.",
                        Toast.LENGTH_LONG
                    ).show()
                    loadPosts()
                }
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

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
