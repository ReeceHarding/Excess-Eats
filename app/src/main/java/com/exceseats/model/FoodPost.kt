package com.exceseats.model

import com.google.firebase.firestore.GeoPoint
import java.util.Date

data class FoodPost(
    val postId: String = "",
    val providerId: String = "",
    val foodDescription: String = "",
    val foodType: String = "",
    val quantity: Int = 0,
    val pickupLocation: GeoPoint? = null,
    val timestamp: Date = Date(),
    val isActive: Boolean = true,
    val imageUrl: String? = null
)
