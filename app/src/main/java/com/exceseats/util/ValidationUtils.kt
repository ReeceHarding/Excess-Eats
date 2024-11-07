package com.exceseats.util

import android.util.Patterns
import com.exceseats.model.FoodPost
import com.exceseats.model.User
import com.google.firebase.firestore.GeoPoint

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8 &&
               password.any { it.isDigit() } &&
               password.any { it.isLetter() }
    }

    fun isValidUser(user: User): Boolean {
        return user.userId.isNotBlank() &&
               user.name.isNotBlank() &&
               isValidEmail(user.email) &&
               user.userType in listOf(Constants.USER_TYPE_CONSUMER, Constants.USER_TYPE_PROVIDER)
    }

    fun isValidFoodPost(foodPost: FoodPost): Boolean {
        return foodPost.postId.isNotBlank() &&
               foodPost.providerId.isNotBlank() &&
               foodPost.foodDescription.isNotBlank() &&
               foodPost.quantity > 0 &&
               foodPost.pickupLocation != null &&
               isValidLocation(foodPost.pickupLocation)
    }

    private fun isValidLocation(location: GeoPoint): Boolean {
        return location.latitude in -90.0..90.0 &&
               location.longitude in -180.0..180.0
    }
}
