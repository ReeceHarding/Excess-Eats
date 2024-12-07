package com.yourcompany.excesseats.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FoodPost(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val foodType: String = FoodCategory.OTHER.displayName,
    val quantity: String = "1 serving",
    val remainingQuantity: Int = 1,
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val pickupTime: Long = 0L,
    val containersAvailable: Boolean = false,
    val isClaimed: Boolean = false,
    val claimedBy: String? = null,
    val claimedAt: Long? = null
) {
    fun getCategory(): FoodCategory {
        return FoodCategory.fromString(foodType)
    }

    fun getEstimatedWeight(): Double {
        return getCategory().getRandomWeight() * remainingQuantity
    }
}
