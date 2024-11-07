package com.exceseats.ui.auth

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.exceseats.databinding.ActivityRegisterBinding
import com.exceseats.ui.home.HomeActivity
import com.exceseats.util.Constants
import com.exceseats.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            val userType = when (binding.userTypeRadioGroup.checkedRadioButtonId) {
                R.id.consumerRadioButton -> Constants.USER_TYPE_CONSUMER
                R.id.providerRadioButton -> Constants.USER_TYPE_PROVIDER
                else -> null
            }

            when {
                name.isBlank() || email.isBlank() || password.isBlank() -> {
                    showToast("Please fill all fields")
                }
                password != confirmPassword -> {
                    showToast("Passwords do not match")
                }
                userType == null -> {
                    showToast("Please select user type")
                }
                else -> {
                    viewModel.register(name, email, password, userType)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.registerState.observe(this) { state ->
            when (state) {
                is RegisterState.Loading -> {
                    binding.registerButton.isEnabled = false
                }
                is RegisterState.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                }
                is RegisterState.Error -> {
                    binding.registerButton.isEnabled = true
                    showToast(state.message)
                }
            }
        }
    }
}
