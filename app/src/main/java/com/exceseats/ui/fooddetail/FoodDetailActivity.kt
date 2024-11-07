package com.exceseats.ui.fooddetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.exceseats.databinding.ActivityFoodDetailBinding
import com.exceseats.util.showToast
import com.exceseats.util.toLatLng
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class FoodDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodDetailBinding
    private val viewModel: FoodDetailViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getStringExtra(EXTRA_POST_ID)
            ?: throw IllegalArgumentException("Post ID is required")

        setupViews()
        observeViewModel()
        viewModel.loadFoodPostDetails(postId)
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewModel.detailState.observe(this) { state ->
            binding.progressBar.isVisible = state is DetailState.Loading
            binding.contentGroup.isVisible = state is DetailState.Success

            when (state) {
                is DetailState.Success -> {
                    val foodPost = state.foodPost
                    val provider = state.provider

                    binding.apply {
                        foodDescriptionTextView.text = foodPost.foodDescription
                        quantityTextView.text = "Quantity: ${foodPost.quantity}"
                        foodTypeTextView.text = "Type: ${foodPost.foodType}"
                        timePostedTextView.text = "Posted: ${dateFormat.format(foodPost.timestamp)}"

                        providerNameTextView.text = "Provider: ${provider?.name ?: "Unknown"}"

                        getDirectionsButton.setOnClickListener {
                            foodPost.pickupLocation?.let { location ->
                                val gmmIntentUri = Uri.parse(
                                    "google.navigation:q=${location.latitude},${location.longitude}"
                                )
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                                    setPackage("com.google.android.apps.maps")
                                }
                                startActivity(mapIntent)
                            }
                        }
                    }
                }
                is DetailState.Error -> {
                    showToast(state.message)
                    finish()
                }
                else -> Unit
            }
        }
    }

    companion object {
        private const val EXTRA_POST_ID = "extra_post_id"

        fun createIntent(context: Context, postId: String): Intent {
            return Intent(context, FoodDetailActivity::class.java).apply {
                putExtra(EXTRA_POST_ID, postId)
            }
        }
    }
}
