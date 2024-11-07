package com.exceseats.ui.postfood

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.exceseats.databinding.ActivityPostFoodBinding
import com.exceseats.util.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostFoodBinding
    private val viewModel: PostFoodViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.postButton.setOnClickListener {
            val description = binding.foodDescriptionEditText.text.toString()
            val foodType = binding.foodTypeSpinner.selectedItem.toString()
            val quantityStr = binding.quantityEditText.text.toString()

            when {
                description.isBlank() -> {
                    showToast("Please enter food description")
                }
                quantityStr.isBlank() -> {
                    showToast("Please enter quantity")
                }
                else -> {
                    getCurrentLocation { location ->
                        viewModel.createFoodPost(
                            description = description,
                            foodType = foodType,
                            quantity = quantityStr.toInt(),
                            location = GeoPoint(location.latitude, location.longitude)
                        )
                    }
                }
            }
        }
    }

    private fun getCurrentLocation(onLocationReceived: (android.location.Location) -> Unit) {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let(onLocationReceived) ?: run {
                        showToast("Unable to get current location")
                    }
                }
                .addOnFailureListener { e ->
                    showToast("Failed to get location: ${e.message}")
                }
        }
    }

    private fun checkLocationPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        } else {
            true
        }
    }

    private fun observeViewModel() {
        viewModel.postState.observe(this) { state ->
            binding.progressBar.isVisible = state is PostFoodState.Loading
            binding.postButton.isEnabled = state !is PostFoodState.Loading

            when (state) {
                is PostFoodState.Success -> {
                    showToast("Food post created successfully")
                    finish()
                }
                is PostFoodState.Error -> {
                    showToast(state.message)
                }
                else -> Unit
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
