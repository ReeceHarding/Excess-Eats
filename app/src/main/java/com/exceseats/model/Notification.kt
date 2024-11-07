package com.exceseats.model

data class Notification(
    val id: String,
    val title: String,
    val description: String,
    val time: Long,
    val read: Boolean,
    val type: NotificationType,
    val data: Map<String, String> = emptyMap()
)

enum class NotificationType {
    NEARBY,
    UPDATE,
    SYSTEM
}
