package com.yourcompany.excesseats.ui.discovery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yourcompany.excesseats.data.model.FoodPost
import com.yourcompany.excesseats.databinding.ItemFoodPostBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ClaimedFoodPostAdapter(
    private val onItemClick: (FoodPost) -> Unit
) : ListAdapter<FoodPost, ClaimedFoodPostAdapter.ViewHolder>(DiffCallback()) {

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
        holder.bind(getItem(position))
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
            binding.apply {
                titleText.text = foodPost.title
                locationText.text = foodPost.location
                timeText.text = "Available until ${timeFormat.format(foodPost.pickupTime)}"
                foodTypeText.text = foodPost.foodType

                // Show remaining servings
                val remainingServings = foodPost.getRemainingServings()
                quantityText.text = "$remainingServings left of ${foodPost.quantity}"

                // Always show as claimed and disable button
                claimButton.text = "Already Claimed"
                claimButton.isEnabled = false

                if (!foodPost.imageUrl.isNullOrEmpty()) {
                    Glide.with(itemView.context)
                        .load(foodPost.imageUrl)
                        .centerCrop()
                        .into(foodImageView)
                }
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<FoodPost>() {
        override fun areItemsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
            return oldItem == newItem
        }
    }
} 