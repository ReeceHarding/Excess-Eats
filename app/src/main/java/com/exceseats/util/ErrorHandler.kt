package com.exceseats.util

import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuthException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor() {
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is FirebaseAuthException -> handleAuthError(throwable)
            is FirebaseException -> handleFirebaseError(throwable)
            else -> throwable.message ?: "An unknown error occurred"
        }
    }

    private fun handleAuthError(error: FirebaseAuthException): String {
        return when (error.errorCode) {
            "ERROR_INVALID_EMAIL" -> "Invalid email address"
            "ERROR_WRONG_PASSWORD" -> "Incorrect password"
            "ERROR_USER_NOT_FOUND" -> "No account found with this email"
            "ERROR_USER_DISABLED" -> "This account has been disabled"
            "ERROR_TOO_MANY_REQUESTS" -> "Too many attempts. Try again later"
            "ERROR_OPERATION_NOT_ALLOWED" -> "Operation not allowed"
            else -> error.message ?: "Authentication failed"
        }
    }

    private fun handleFirebaseError(error: FirebaseException): String {
        return when {
            error.message?.contains("PERMISSION_DENIED") == true ->
                "You don't have permission to perform this action"
            error.message?.contains("NETWORK") == true ->
                "Network error. Please check your connection"
            else -> error.message ?: "A Firebase error occurred"
        }
    }
}
