package com.yourcompany.excesseats

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class ExcessEatsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Enable offline persistence for Realtime Database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Initialize Places SDK
        Places.initialize(applicationContext, getString(R.string.google_maps_key))
    }
}
