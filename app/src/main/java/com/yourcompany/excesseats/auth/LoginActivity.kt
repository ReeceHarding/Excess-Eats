package com.yourcompany.excesseats.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yourcompany.excesseats.MainActivity
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val TAG = "LoginActivity"
    private val userRepository = UserRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Starting onCreate")

            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d(TAG, "Layout inflated")

            binding.btnLogin.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    lifecycleScope.launch {
                        val result = userRepository.getUserByEmail(email)
                        when {
                            result.isSuccess -> {
                                val user = result.getOrNull()
                                if (user != null) {
                                    // In a real app, we would hash the password and compare
                                    if (password == user.password) {
                                        Log.d(TAG, "Login successful")
                                        startMainActivity()
                                    } else {
                                        Log.w(TAG, "Incorrect password")
                                        Toast.makeText(this@LoginActivity,
                                            "Incorrect password",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            result.isFailure -> {
                                Log.w(TAG, "User not found")
                                Toast.makeText(this@LoginActivity,
                                    "Email not registered. Please create an account.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
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
