package com.yourcompany.excesseats.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yourcompany.excesseats.MainActivity
import com.yourcompany.excesseats.data.model.User
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import java.util.UUID

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val userRepository = UserRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val displayName = binding.etDisplayName.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && displayName.isNotEmpty()) {
                // Simple validation
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (password.length < 6) {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Check if email already exists
                lifecycleScope.launch {
                    val existingUser = userRepository.getUserByEmail(email)
                    if (existingUser.isSuccess) {
                        Toast.makeText(this@RegisterActivity,
                            "Email already registered. Please login.",
                            Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Create user and proceed
                    createUserProfile(email, password, displayName)
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            finish()
        }
    }

    private suspend fun createUserProfile(email: String, password: String, displayName: String) {
        val user = User(
            id = UUID.randomUUID().toString(),
            email = email,
            password = password, // In a real app, this would be hashed
            displayName = displayName
        )

        val result = userRepository.createUser(user)
        if (result.isSuccess) {
            startMainActivity()
        } else {
            Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
