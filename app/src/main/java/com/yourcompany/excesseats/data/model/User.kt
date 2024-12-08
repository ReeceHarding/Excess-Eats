package com.yourcompany.excesseats.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val profileImageUrl: String? = null,
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val role: UserRole = UserRole.SEEKER,
    val createdAt: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis()
)

data class NotificationPreferences(
    val newItemsNearby: Boolean = true,
    val itemStatusUpdates: Boolean = true,
    val reminders: Boolean = true,
    val marketing: Boolean = false,
    val radius: Double = 5.0 // in kilometers
)

enum class UserRole {
    SEEKER,
    PROVIDER,
    BOTH
}
