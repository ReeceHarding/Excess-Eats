package com.exceseats.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exceseats.databinding.ItemFoodPostBinding
import com.exceseats.model.FoodPost
import java.text.SimpleDateFormat
import java.util.Locale

class FoodPostAdapter(
    private val onItemClick: (FoodPost) -> Unit
) : ListAdapter<FoodPost, FoodPostAdapter.FoodPostViewHolder>(FoodPostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPostViewHolder {
        val binding = ItemFoodPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodPostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodPostViewHolder(
        private val binding: ItemFoodPostBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

        fun bind(foodPost: FoodPost) {
            binding.apply {
                foodDescriptionTextView.text = foodPost.foodDescription
                quantityTextView.text = "Quantity: ${foodPost.quantity}"
                timePostedTextView.text = "Posted: ${dateFormat.format(foodPost.timestamp)}"

                root.setOnClickListener { onItemClick(foodPost) }
            }
        }
    }
}

private class FoodPostDiffCallback : DiffUtil.ItemCallback<FoodPost>() {
    override fun areItemsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
        return oldItem.postId == newItem.postId
    }

    override fun areContentsTheSame(oldItem: FoodPost, newItem: FoodPost): Boolean {
        return oldItem == newItem
    }
}
