package com.yourcompany.excesseats.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

            // Check if user is already signed in
            if (userRepository.getCurrentUser() != null) {
                startMainActivity()
                return
            }

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

                    // Show loading state
                    setLoading(true)

                    lifecycleScope.launch {
                        try {
                            val result = userRepository.signIn(email, password)
                            when {
                                result.isSuccess -> {
                                    Log.d(TAG, "Login successful")
                                    startMainActivity()
                                }
                                result.isFailure -> {
                                    val exception = result.exceptionOrNull()
                                    Log.w(TAG, "Login failed", exception)
                                    Toast.makeText(
                                        this@LoginActivity,
                                        exception?.message ?: "Login failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error during login", e)
                            Toast.makeText(
                                this@LoginActivity,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } finally {
                            setLoading(false)
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

    private fun setLoading(isLoading: Boolean) {
        binding.btnLogin.isEnabled = !isLoading
        binding.btnRegister.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
