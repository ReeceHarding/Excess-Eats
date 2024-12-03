package com.yourcompany.excesseats.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validators {
    // Email validation
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Password validation
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         // at least 1 digit
                    "(?=.*[a-z])" +         // at least 1 lower case letter
                    "(?=.*[A-Z])" +         // at least 1 upper case letter
                    "(?=.*[!@#$%^&*])" +    // at least 1 special character
                    "(?=\\S+$)" +           // no white spaces
                    ".{8,}" +               // at least 8 characters
                    "$"
        )
        return passwordPattern.matcher(password).matches()
    }

    // Phone number validation
    fun isValidPhoneNumber(phone: String): Boolean {
        return phone.isNotEmpty() && Patterns.PHONE.matcher(phone).matches()
    }

    // Item title validation
    fun isValidTitle(title: String): Boolean {
        return title.isNotEmpty() && title.length <= Constants.MAX_TITLE_LENGTH
    }

    // Item description validation
    fun isValidDescription(description: String): Boolean {
        return description.isNotEmpty() && description.length <= Constants.MAX_DESCRIPTION_LENGTH
    }

    // Quantity validation
    fun isValidQuantity(quantity: String): Boolean {
        return quantity.isNotEmpty() && quantity.toIntOrNull()?.let { it > 0 } ?: false
    }

    // Location validation
    fun isValidLocation(latitude: Double, longitude: Double): Boolean {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180
    }

    // Time validation
    fun isValidPickupTime(pickupTime: Long): Boolean {
        val now = System.currentTimeMillis()
        return pickupTime > now
    }

    // Image size validation
    fun isValidImageSize(sizeInBytes: Long): Boolean {
        return sizeInBytes <= Constants.MAX_IMAGE_SIZE
    }

    // Name validation
    fun isValidName(name: String): Boolean {
        val namePattern = Pattern.compile("^[a-zA-Z\\s]{2,30}$")
        return namePattern.matcher(name).matches()
    }

    // URL validation
    fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    // Postal code validation (US format)
    fun isValidPostalCode(postalCode: String): Boolean {
        val postalPattern = Pattern.compile("^\\d{5}(-\\d{4})?$")
        return postalPattern.matcher(postalCode).matches()
    }

    // Date validation
    fun isValidDate(date: Long): Boolean {
        val now = System.currentTimeMillis()
        return date >= now
    }

    // Time range validation
    fun isValidTimeRange(startTime: Long, endTime: Long): Boolean {
        return startTime < endTime && isValidDate(startTime)
    }

    // Search radius validation
    fun isValidSearchRadius(radius: Double): Boolean {
        return radius > 0 && radius <= Constants.MAX_SEARCH_RADIUS_KM
    }

    // Rating validation
    fun isValidRating(rating: Float): Boolean {
        return rating in 0f..5f
    }

    // Input sanitization
    fun sanitizeInput(input: String): String {
        return input.trim()
            .replace("[<>]".toRegex(), "")
            .replace("\\s+".toRegex(), " ")
    }

    // Error Messages
    fun getPasswordErrorMessage(password: String): String {
        return when {
            password.isEmpty() -> "Password cannot be empty"
            password.length < 8 -> "Password must be at least 8 characters long"
            !password.contains(Regex("[0-9]")) -> "Password must contain at least one digit"
            !password.contains(Regex("[a-z]")) -> "Password must contain at least one lowercase letter"
            !password.contains(Regex("[A-Z]")) -> "Password must contain at least one uppercase letter"
            !password.contains(Regex("[!@#\$%^&*]")) -> "Password must contain at least one special character"
            password.contains(" ") -> "Password cannot contain spaces"
            else -> ""
        }
    }

    fun getEmailErrorMessage(email: String): String {
        return when {
            email.isEmpty() -> "Email cannot be empty"
            !isValidEmail(email) -> "Please enter a valid email address"
            else -> ""
        }
    }

    fun getPhoneErrorMessage(phone: String): String {
        return when {
            phone.isEmpty() -> "Phone number cannot be empty"
            !isValidPhoneNumber(phone) -> "Please enter a valid phone number"
            else -> ""
        }
    }
}
