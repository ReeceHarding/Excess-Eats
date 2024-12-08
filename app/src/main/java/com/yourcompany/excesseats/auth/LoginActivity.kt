package com.yourcompany.excesseats.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yourcompany.excesseats.MainActivity
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.databinding.ActivityLoginBinding
import com.yourcompany.excesseats.utils.Logger
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val userRepository = UserRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            // Check if user is already signed in
            if (userRepository.getCurrentUser() != null) {
                startMainActivity()
                return
            }

            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

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
                                    startMainActivity()
                                }
                                result.isFailure -> {
                                    val exception = result.exceptionOrNull()
                                    Toast.makeText(
                                        this@LoginActivity,
                                        exception?.message ?: "Login failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
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
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnRegister.setOnClickListener {
                Logger.d("Starting RegisterActivity")
                startActivity(Intent(this, RegisterActivity::class.java))
            }

            Logger.d("onCreate completed")
        } catch (e: Exception) {
            Logger.e("Error in onCreate", e)
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnLogin.isEnabled = !isLoading
        binding.btnRegister.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startMainActivity() {
        Logger.d("Starting MainActivity")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        Logger.d("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("onPause called")
    }
}
