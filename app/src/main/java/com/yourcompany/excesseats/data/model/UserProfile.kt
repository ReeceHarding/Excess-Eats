package com.yourcompany.excesseats.data.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val stats: UserStats = UserStats()
)

data class UserStats(
    val mealsClaimedCount: Int = 0,
    val wasteSavedPounds: Double = 0.0
)
