package com.yourcompany.excesseats.ui.discovery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.yourcompany.excesseats.R
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.databinding.FragmentFoodPostDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FoodPostDetailFragment : Fragment() {

    private var _binding: FragmentFoodPostDetailBinding? = null
    private val binding get() = _binding!!
    private var googleMap: GoogleMap? = null
    private val args: FoodPostDetailFragmentArgs by navArgs()
    private val repository = FoodPostRepository.getInstance()
    private var isClaimed = false

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
        setupToolbar()
        setupMap(savedInstanceState)
        loadFoodPost()
        setupButtons()
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
            repository.getFoodPost(args.postId).collectLatest { result ->
                result.onSuccess { post ->
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
    }

    private fun updateUI(post: FoodPost) {
        binding.apply {
            titleText.text = post.title
            foodTypeChip.text = post.foodType
            locationText.text = post.location
            timeText.text = "Available until ${SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(post.pickupTime))}"
            quantityText.text = post.quantity
            postedByText.text = "Posted by: ${post.userId}"
            containerText.text = if (post.containersAvailable) "Containers available" else "Bring your own container"
            descriptionText.text = post.description

            // Load food image
            Glide.with(requireContext())
                .load(post.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .centerCrop()
                .into(foodImageView)

            updateMapLocation()
        }
    }

    private fun updateMapLocation() {
        googleMap?.let { map ->
            val location = LatLng(args.latitude.toDouble(), args.longitude.toDouble())
            map.clear()
            map.addMarker(MarkerOptions().position(location).title(args.title))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
    }

    private fun setupButtons() {
        binding.claimButton.setOnClickListener {
            if (!isClaimed) {
                lifecycleScope.launch {
                    val result = repository.claimFoodPost(args.postId)
                    result.onSuccess {
                        isClaimed = true
                        binding.claimButton.text = "Claimed"
                        binding.claimButton.isEnabled = false
                        Toast.makeText(
                            requireContext(),
                            "Successfully claimed food!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.onFailure { exception ->
                        Toast.makeText(
                            requireContext(),
                            "Failed to claim food: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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
