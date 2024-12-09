package com.yourcompany.excesseats

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Starting onCreate")

            _binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupNavigation()
            migrateDatabase()
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupNavigation() {
        try {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment

            if (navHostFragment == null) {
                Log.e(TAG, "NavHostFragment is null")
                Toast.makeText(this, "Error initializing navigation", Toast.LENGTH_LONG).show()
                return
            }

            val navController = navHostFragment.navController

            binding.bottomNavigationView.setupWithNavController(navController)
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up navigation", e)
        }
    }

    private fun migrateDatabase() {
        lifecycleScope.launch {
            try {
                val repository = FoodPostRepository.getInstance()
                repository.migrateExistingPosts()
                    .onFailure { e ->
                        Log.e(TAG, "Failed to migrate posts", e)
                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error migrating database", e)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
