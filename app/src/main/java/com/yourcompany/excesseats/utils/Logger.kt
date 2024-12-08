package com.yourcompany.excesseats.utils

import android.util.Log

object Logger {
    private const val TAG = "ExcessEats"
    private var isDebugEnabled = false

    fun setDebugEnabled(enabled: Boolean) {
        isDebugEnabled = enabled
    }

    fun e(message: String, throwable: Throwable? = null) {
        // Always log errors
        if (throwable != null) {
            Log.e(TAG, message, throwable)
        } else {
            Log.e(TAG, message)
        }
    }

    fun d(message: String) {
        // Only log debug messages if debug is enabled
        if (isDebugEnabled) {
            Log.d(TAG, message)
        }
    }

    fun i(message: String) {
        // Only log info messages if debug is enabled
        if (isDebugEnabled) {
            Log.i(TAG, message)
        }
    }
}
