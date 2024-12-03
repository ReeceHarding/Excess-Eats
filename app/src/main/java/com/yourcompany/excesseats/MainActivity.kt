package com.yourcompany.excesseats

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.yourcompany.excesseats.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Starting onCreate")

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d(TAG, "Layout inflated")

            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            Log.d(TAG, "NavHostFragment found: ${navHostFragment != null}")

            if (navHostFragment == null) {
                Toast.makeText(this, "Error initializing navigation", Toast.LENGTH_LONG).show()
                Log.e(TAG, "NavHostFragment is null")
                return
            }

            val navController = navHostFragment.navController
            Log.d(TAG, "NavController initialized")

            binding.bottomNavigation.setupWithNavController(navController)
            Log.d(TAG, "Bottom navigation setup complete")

            // Set up navigation error handling
            navController.addOnDestinationChangedListener { _, destination, _ ->
                Log.d(TAG, "Navigation changed to: ${destination.label}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }
}
