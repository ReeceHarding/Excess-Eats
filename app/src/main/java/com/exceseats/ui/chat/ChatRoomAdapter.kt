package com.exceseats.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.exceseats.databinding.ItemChatRoomBinding
import com.exceseats.model.ChatRoom
import com.exceseats.util.DateTimeUtils

class ChatRoomAdapter(
    private val onRoomClick: (ChatRoom) -> Unit
) : ListAdapter<ChatRoom, ChatRoomAdapter.ChatRoomViewHolder>(ChatRoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val binding = ItemChatRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ChatRoomViewHolder(
        private val binding: ItemChatRoomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(room: ChatRoom) {
            binding.apply {
                lastMessageTextView.text = room.lastMessage
                timeTextView.text = DateTimeUtils.getTimeAgo(room.lastMessageTime)
                unreadCountTextView.isVisible = room.unreadCount > 0
                unreadCountTextView.text = room.unreadCount.toString()

                root.setOnClickListener { onRoomClick(room) }
            }
        }
    }
}

private class ChatRoomDiffCallback : DiffUtil.ItemCallback<ChatRoom>() {
    override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom) =
        oldItem.roomId == newItem.roomId

    override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom) =
        oldItem == newItem
}
