package com.exceseats.model

data class ChatRoom(
    val roomId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Map<String, Int> = emptyMap()
)
