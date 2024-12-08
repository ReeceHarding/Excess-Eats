package com.yourcompany.excesseats.data.model

import com.google.firebase.database.IgnoreExtraProperties
import kotlin.math.max

@IgnoreExtraProperties
data class FoodPost(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val foodType: String = "",
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
    val claimedAt: Long? = null,
    val claimedByUsers: Map<String, ClaimInfo> = emptyMap()  // Map of userId to claim info
) {
    fun isClaimedByUser(userId: String): Boolean {
        return claimedByUsers.containsKey(userId)
    }

    fun getRemainingServings(): Int {
        val totalClaims = claimedByUsers.values.sumOf { it.servingsClaimed }
        return max(0, remainingQuantity - totalClaims)
    }

    fun getEstimatedWeight(): Double {
        val category = try {
            FoodCategory.valueOf(foodType.uppercase().replace(" ", "_"))
        } catch (e: IllegalArgumentException) {
            FoodCategory.OTHER
        }
        return category.getRandomWeight() * remainingQuantity
    }
}

data class ClaimInfo(
    val userId: String = "",
    val claimedAt: Long = 0L,
    val servingsClaimed: Int = 1
)
