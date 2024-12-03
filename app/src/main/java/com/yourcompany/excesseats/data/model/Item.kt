package com.yourcompany.excesseats.data.model

data class Item(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val foodType: FoodType = FoodType.OTHER,
    val quantity: String = "",
    val servings: Int = 1,
    val location: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String? = null,
    val providerId: String = "",
    val providerName: String = "",
    val providerRating: Float = 0f,
    val status: ItemStatus = ItemStatus.AVAILABLE,
    val pickupInstructions: String = "",
    val pickupTimeStart: Long = 0L,
    val pickupTimeEnd: Long = 0L,
    val containerAvailable: Boolean = false,
    val allergenInfo: List<String> = emptyList(),
    val dietaryInfo: List<String> = emptyList(),
    val claimedBy: String? = null,
    val claimedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val expiresAt: Long? = null
)

enum class FoodType {
    PREPARED_MEAL,
    GROCERIES,
    BAKED_GOODS,
    PRODUCE,
    DAIRY,
    MEAT,
    PANTRY,
    BEVERAGES,
    OTHER
}

enum class ItemStatus {
    AVAILABLE,
    PENDING,
    CLAIMED,
    COMPLETED,
    EXPIRED,
    CANCELLED
}

data class ItemLocation(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val instructions: String = ""
)
