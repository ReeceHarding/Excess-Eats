package com.yourcompany.excesseats.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yourcompany.excesseats.MainActivity
import com.yourcompany.excesseats.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Starting onCreate")

            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d(TAG, "Layout inflated")

            // Temporary: Skip authentication and go straight to MainActivity
            binding.btnLogin.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    Log.d(TAG, "Login successful (temporary no-auth mode)")
                    startMainActivity()
                } else {
                    Log.w(TAG, "Empty email or password")
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnRegister.setOnClickListener {
                Log.d(TAG, "Starting RegisterActivity")
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            Log.d(TAG, "onCreate completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun startMainActivity() {
        Log.d(TAG, "Starting MainActivity")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
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
