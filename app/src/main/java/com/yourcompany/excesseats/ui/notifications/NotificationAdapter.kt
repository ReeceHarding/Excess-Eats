package com.yourcompany.excesseats.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourcompany.excesseats.data.model.Notification
import com.yourcompany.excesseats.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(
    private val onNotificationClick: (Notification) -> Unit
) : ListAdapter<Notification, NotificationAdapter.ViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(
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
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

        fun bind(notification: Notification) {
            binding.apply {
                titleText.text = notification.title
                messageText.text = notification.message
                timeText.text = dateFormat.format(Date(notification.timestamp))
                root.alpha = if (notification.isRead) 0.6f else 1.0f
                
                root.setOnClickListener {
                    onNotificationClick(notification)
                }
            }
        }
    }

    private class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
} 