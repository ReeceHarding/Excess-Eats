package com.yourcompany.excesseats.ui.discovery

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
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
import com.yourcompany.excesseats.R
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

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(foodPost: FoodPost) {
            Log.d("FoodPostAdapter", "Binding food post: ${foodPost.title}")
            binding.apply {
                titleText.text = foodPost.title
                locationText.text = foodPost.location
                timeText.text = "Available until ${timeFormat.format(foodPost.pickupTime)}"
                foodTypeText.text = foodPost.foodType

                // Load food image
                Log.d("FoodPostAdapter", "Loading image from URL: ${foodPost.imageUrl}")
                if (foodPost.imageUrl.isEmpty()) {
                    Log.d("FoodPostAdapter", "Empty imageUrl, showing placeholder")
                    foodImageView.setImageResource(android.R.drawable.ic_menu_gallery)
                } else {
                    Log.d("FoodPostAdapter", "Attempting to load image with Glide from URL: ${foodPost.imageUrl}")
                    Glide.with(foodImageView)
                        .load(foodPost.imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("FoodPostAdapter", "Failed to load image for post ${foodPost.id} from URL ${foodPost.imageUrl}", e)
                                e?.logRootCauses("FoodPostAdapter")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.d("FoodPostAdapter", "Successfully loaded image for post ${foodPost.id} from URL ${foodPost.imageUrl}")
                                return false
                            }
                        })
                        .into(foodImageView)
                }

                // Calculate and display distance if user location is available
                userLocation?.let { location ->
                    val distance = calculateDistance(
                        location.latitude, location.longitude,
                        foodPost.latitude, foodPost.longitude
                    )
                    distanceText.text = "${distance.roundToInt()} km away"
                }

                claimButton.setOnClickListener {
                    onItemClick(foodPost)
                }
            }
        }
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(Math.toRadians(lat1)) * kotlin.math.cos(Math.toRadians(lat2)) *
                kotlin.math.sin(dLon / 2) * kotlin.math.sin(dLon / 2)
        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return r * c
    }

    private class FoodPostDiffCallback : DiffUtil.ItemCallback<FoodPost>() {
        override fun areItemsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
            return oldItem == newItem
        }
    }
}
