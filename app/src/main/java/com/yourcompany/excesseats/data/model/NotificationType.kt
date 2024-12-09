package com.yourcompany.excesseats.data.model

enum class NotificationType {
    NEW_FOOD_NEARBY,
    FOOD_CLAIMED,
    FOOD_EXPIRED,
    SYSTEM_MESSAGE;

    fun getValue(): String = name

    companion object {
        fun fromString(value: String): NotificationType {
            return try {
                valueOf(value)
            } catch (e: IllegalArgumentException) {
                SYSTEM_MESSAGE
            }
        }
    }
}
