package com.exceseats.model

import com.google.firebase.firestore.GeoPoint

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val userType: String = "",
    val location: GeoPoint? = null,
    val preferences: Map<String, Any> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis()
)
