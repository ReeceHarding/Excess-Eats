package com.yourcompany.excesseats.utils

object Constants {
    // Firebase Collections
    const val COLLECTION_USERS = "users"
    const val COLLECTION_ITEMS = "items"
    const val COLLECTION_PREFERENCES = "preferences"
    const val COLLECTION_NOTIFICATIONS = "notifications"

    // Storage Paths
    const val STORAGE_PROFILE_IMAGES = "profile_images"
    const val STORAGE_ITEM_IMAGES = "item_images"

    // Shared Preferences
    const val PREFS_NAME = "ExcessEatsPrefs"
    const val PREF_USER_ID = "userId"
    const val PREF_USER_EMAIL = "userEmail"
    const val PREF_USER_DISPLAY_NAME = "userDisplayName"
    const val PREF_USER_ROLE = "userRole"
    const val PREF_NOTIFICATION_ENABLED = "notificationEnabled"
    const val PREF_LOCATION_ENABLED = "locationEnabled"

    // Intent Keys
    const val EXTRA_ITEM_ID = "itemId"
    const val EXTRA_USER_ID = "userId"
    const val EXTRA_NOTIFICATION_ID = "notificationId"

    // Request Codes
    const val RC_SIGN_IN = 9001
    const val RC_LOCATION_PERMISSION = 9002
    const val RC_CAMERA_PERMISSION = 9003
    const val RC_STORAGE_PERMISSION = 9004
    const val RC_IMAGE_PICK = 9005
    const val RC_IMAGE_CAPTURE = 9006

    // Notification Channels
    const val CHANNEL_NEW_ITEMS = "new_items"
    const val CHANNEL_ITEM_UPDATES = "item_updates"
    const val CHANNEL_REMINDERS = "reminders"
    const val CHANNEL_GENERAL = "general"

    // Time Constants
    const val HOUR_IN_MILLIS = 60 * 60 * 1000L
    const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS
    const val WEEK_IN_MILLIS = 7 * DAY_IN_MILLIS

    // Location Constants
    const val DEFAULT_SEARCH_RADIUS_KM = 5.0
    const val MAX_SEARCH_RADIUS_KM = 50.0
    const val LOCATION_UPDATE_INTERVAL = 5 * 60 * 1000L // 5 minutes
    const val FASTEST_LOCATION_INTERVAL = 60 * 1000L // 1 minute

    // UI Constants
    const val DEFAULT_PAGE_SIZE = 20
    const val MAX_IMAGE_SIZE = 1024 * 1024 // 1MB
    const val IMAGE_COMPRESSION_QUALITY = 85
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_TITLE_LENGTH = 100
    const val MAX_DESCRIPTION_LENGTH = 500

    // Error Messages
    const val ERROR_NETWORK = "Network error occurred. Please check your connection."
    const val ERROR_UNKNOWN = "An unknown error occurred. Please try again."
    const val ERROR_INVALID_CREDENTIALS = "Invalid email or password."
    const val ERROR_WEAK_PASSWORD = "Password should be at least 8 characters long."
    const val ERROR_EMAIL_IN_USE = "This email is already registered."
    const val ERROR_INVALID_EMAIL = "Please enter a valid email address."
    const val ERROR_LOCATION_PERMISSION = "Location permission is required for this feature."
    const val ERROR_CAMERA_PERMISSION = "Camera permission is required for this feature."
    const val ERROR_STORAGE_PERMISSION = "Storage permission is required for this feature."

    // Success Messages
    const val SUCCESS_ITEM_POSTED = "Item posted successfully!"
    const val SUCCESS_ITEM_CLAIMED = "Item claimed successfully!"
    const val SUCCESS_PROFILE_UPDATED = "Profile updated successfully!"
    const val SUCCESS_PASSWORD_RESET = "Password reset email sent successfully!"
}
