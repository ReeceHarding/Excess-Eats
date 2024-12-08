package com.yourcompany.excesseats.ui.discovery

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.databinding.ItemFoodPostBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class FoodPostAdapter(
    private val onItemClick: (FoodPost) -> Unit,
    var userLocation: LatLng? = null
) : ListAdapter<FoodPost, FoodPostAdapter.ViewHolder>(FoodPostDiffCallback()) {

    private val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        Log.d("FoodPostAdapter", "Binding post at position $position: ${post.title}")
        holder.bind(post)
    }

    inner class ViewHolder(
        private val binding: ItemFoodPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(foodPost: FoodPost) {
            binding.apply {
                titleText.text = foodPost.title
                locationText.text = foodPost.location
                timeText.text = "Available until ${timeFormat.format(foodPost.pickupTime)}"
                foodTypeText.text = foodPost.foodType

                // Load food image
                if (foodPost.imageUrl.isEmpty()) {
                    foodImageView.setImageResource(android.R.drawable.ic_menu_gallery)
                } else {
                    try {
                        Glide.with(foodImageView)
                            .load(foodPost.imageUrl)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .error(android.R.drawable.ic_dialog_alert)
                            .into(foodImageView)
                    } catch (e: Exception) {
                        Log.e("FoodPostAdapter", "Error loading image", e)
                        foodImageView.setImageResource(android.R.drawable.ic_dialog_alert)
                    }
                }

                // Calculate and display distance if user location is available
                userLocation?.let { location ->
                    val distance = calculateDistance(
                        location.latitude, location.longitude,
                        foodPost.latitude, foodPost.longitude
                    )
                    distanceText.text = "${distance.roundToInt()} km away"
                }

                // Update quantity text to show remaining
                val remainingServings = foodPost.getRemainingServings()
                val quantityString = if (remainingServings > 0) {
                    "$remainingServings left of ${foodPost.quantity}"
                } else {
                    "Fully claimed"
                }
                quantityText.text = quantityString

                // Handle claimed status
                currentUserId?.let { userId ->
                    val isClaimedByCurrentUser = foodPost.isClaimedByUser(userId)
                    claimedChip.visibility = if (isClaimedByCurrentUser) View.VISIBLE else View.GONE
                    claimedOverlay.visibility = if (isClaimedByCurrentUser) View.VISIBLE else View.GONE

                    // Update claim button state
                    claimButton.apply {
                        isEnabled = remainingServings > 0 && !isClaimedByCurrentUser
                        text = when {
                            isClaimedByCurrentUser -> "Claimed by you"
                            remainingServings <= 0 -> "Fully claimed"
                            else -> "Claim"
                        }
                    }
                }

                // Add click listener
                root.setOnClickListener {
                    onItemClick(foodPost)
                }
            }
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }
}

private class FoodPostDiffCallback : DiffUtil.ItemCallback<FoodPost>() {
    override fun areItemsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
        return oldItem == newItem
    }
}
