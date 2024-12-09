package com.yourcompany.excesseats.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourcompany.excesseats.databinding.ItemNotificationBinding
import com.yourcompany.excesseats.models.Notification
import java.text.SimpleDateFormat
import java.util.Locale

class NotificationAdapter(
    private val onNotificationClick: (Notification) -> Unit,
    private val onMarkAsRead: (Notification) -> Unit,
    private val onDelete: (Notification) -> Unit
) : ListAdapter<Notification, NotificationAdapter.ViewHolder>(NotificationDiffCallback()) {

    class ViewHolder(
        private val binding: ItemNotificationBinding,
        private val onNotificationClick: (Notification) -> Unit,
        private val onMarkAsRead: (Notification) -> Unit,
        private val onDelete: (Notification) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val timeFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

        fun bind(notification: Notification) {
            binding.textTitle.text = notification.title
            binding.textMessage.text = notification.message
            binding.textTimestamp.text = timeFormat.format(notification.timestamp)

            // Handle click
            binding.root.setOnClickListener {
                onNotificationClick(notification)
                if (!notification.isRead) {
                    onMarkAsRead(notification)
                }
            }

            // Delete button click
            binding.buttonDelete.setOnClickListener {
                onDelete(notification)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onNotificationClick, onMarkAsRead, onDelete)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
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
