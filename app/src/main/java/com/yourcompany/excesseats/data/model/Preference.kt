package com.yourcompany.excesseats.data.model

data class Preference(
    val userId: String = "",
    val dietaryPreferences: List<DietaryPreference> = emptyList(),
    val allergens: List<Allergen> = emptyList(),
    val preferredFoodTypes: List<FoodType> = emptyList(),
    val maxDistance: Double = 5.0, // in kilometers
    val notificationSettings: NotificationSettings = NotificationSettings(),
    val privacySettings: PrivacySettings = PrivacySettings()
)

enum class DietaryPreference {
    VEGETARIAN,
    VEGAN,
    HALAL,
    KOSHER,
    GLUTEN_FREE,
    DAIRY_FREE,
    NUT_FREE,
    ORGANIC,
    NONE
}

enum class Allergen {
    MILK,
    EGGS,
    FISH,
    SHELLFISH,
    TREE_NUTS,
    PEANUTS,
    WHEAT,
    SOYBEANS,
    NONE
}

data class NotificationSettings(
    val pushEnabled: Boolean = true,
    val emailEnabled: Boolean = true,
    val smsEnabled: Boolean = false,
    val newItemAlerts: Boolean = true,
    val statusUpdates: Boolean = true,
    val reminderNotifications: Boolean = true,
    val marketingNotifications: Boolean = false,
    val quietHoursEnabled: Boolean = false,
    val quietHoursStart: Int = 22, // 24-hour format
    val quietHoursEnd: Int = 7 // 24-hour format
)

data class PrivacySettings(
    val showProfile: Boolean = true,
    val showLocation: Boolean = true,
    val showContactInfo: Boolean = false,
    val allowMessaging: Boolean = true,
    val showActivityHistory: Boolean = true
)
