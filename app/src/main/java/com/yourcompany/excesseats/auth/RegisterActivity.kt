package com.yourcompany.excesseats.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
    private val TAG = "RegisterActivity"
    private val userRepository = UserRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            Log.d(TAG, "Starting onCreate")

            binding = ActivityRegisterBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d(TAG, "Layout inflated")

            setupPhoneNumberFormatting()

            binding.btnRegister.setOnClickListener {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val name = binding.etDisplayName.text.toString()
                val phone = binding.etPhone.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && phone.isNotEmpty()) {
                    // Simple validation
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (password.length < 6) {
                        Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (!isValidPhoneNumber(phone)) {
                        Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Show loading state
                    setLoading(true)

                    // Create user profile
                    val user = User(
                        id = UUID.randomUUID().toString(), // This will be replaced with Firebase UID
                        email = email,
                        password = password,
                        name = name,
                        phone = phone.replace(Regex("[^0-9]"), "") // Store only digits
                    )

                    lifecycleScope.launch {
                        try {
                            val result = userRepository.createUser(user)
                            when {
                                result.isSuccess -> {
                                    Log.d(TAG, "Registration successful")
                                    startMainActivity()
                                }
                                result.isFailure -> {
                                    val exception = result.exceptionOrNull()
                                    Log.w(TAG, "Registration failed", exception)
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        exception?.message ?: "Registration failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error during registration", e)
                            Toast.makeText(
                                this@RegisterActivity,
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

            binding.btnLogin.setOnClickListener {
                finish()
            }

            Log.d(TAG, "onCreate completed")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate", e)
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupPhoneNumberFormatting() {
        var isFormatting = false
        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                // Remove all non-digit characters
                val digits = s.toString().replace(Regex("[^0-9]"), "")

                // Format the phone number
                val formatted = when {
                    digits.length <= 3 -> digits
                    digits.length <= 6 -> "(${digits.substring(0, 3)}) ${digits.substring(3)}"
                    else -> "(${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6, minOf(digits.length, 10))}"
                }

                // Update the text if it's different
                if (formatted != s.toString()) {
                    s?.replace(0, s.length, formatted)
                }

                isFormatting = false
            }
        })
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val digits = phone.replace(Regex("[^0-9]"), "")
        return digits.length == 10
    }

    private fun setLoading(isLoading: Boolean) {
        binding.btnRegister.isEnabled = !isLoading
        binding.btnLogin.isEnabled = !isLoading
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}
