package com.yourcompany.excesseats

import android.app.Application
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.yourcompany.excesseats.utils.Logger

class ExcessEatsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enable debug logging in debug builds
        Logger.setDebugEnabled(BuildConfig.DEBUG)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Enable offline persistence for Realtime Database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize Places SDK
        Places.initialize(applicationContext, getString(R.string.google_maps_key))

        // Set up global error handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Logger.e("Uncaught exception in thread $thread", throwable)
        }
    }
}
