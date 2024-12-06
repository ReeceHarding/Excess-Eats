package com.yourcompany.excesseats.data.model

data class Notification(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val message: String,
    val postId: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
) 