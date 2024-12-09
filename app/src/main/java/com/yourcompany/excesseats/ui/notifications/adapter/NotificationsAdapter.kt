package com.yourcompany.excesseats.ui.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourcompany.excesseats.databinding.ItemNotificationBinding
import com.yourcompany.excesseats.models.Notification
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationsAdapter(
    private val onNotificationClick: (Notification) -> Unit,
    private val onDeleteClick: (Notification) -> Unit
) : ListAdapter<Notification, NotificationsAdapter.ViewHolder>(NotificationDiffCallback()) {

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

        private val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

        fun bind(notification: Notification) {
            binding.apply {
                textTitle.text = notification.title
                textMessage.text = notification.message
                textTimestamp.text = dateFormat.format(notification.timestamp)

                // Set read/unread state
                root.alpha = if (notification.isRead) 0.7f else 1.0f

                // Click listeners
                root.setOnClickListener { onNotificationClick(notification) }
                buttonDelete.setOnClickListener { onDeleteClick(notification) }
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
