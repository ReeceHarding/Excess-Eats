package com.exceseats.util

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsUtils @Inject constructor(
    private val analytics: FirebaseAnalytics
) {
    fun logEvent(eventName: String, params: Bundle? = null) {
        analytics.logEvent(eventName, params)
    }

    fun setUserProperty(name: String, value: String) {
        analytics.setUserProperty(name, value)
    }

    companion object {
        // Screen Views
        const val SCREEN_HOME = "screen_home"
        const val SCREEN_MAP = "screen_map"
        const val SCREEN_FOOD_DETAIL = "screen_food_detail"

        // User Actions
        const val EVENT_FOOD_POST_CREATED = "food_post_created"
        const val EVENT_FOOD_CLAIMED = "food_claimed"
        const val EVENT_GET_DIRECTIONS = "get_directions"

        // User Properties
        const val USER_TYPE = "user_type"
        const val NOTIFICATION_RADIUS = "notification_radius"
    }
}
