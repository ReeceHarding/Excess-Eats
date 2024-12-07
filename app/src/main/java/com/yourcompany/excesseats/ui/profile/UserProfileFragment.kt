package com.yourcompany.excesseats.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yourcompany.excesseats.auth.LoginActivity
import com.yourcompany.excesseats.data.model.UserProfile
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.databinding.FragmentUserProfileBinding
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        loadUserProfile()
    }

    private fun setupViews() {
        binding.apply {
            // Profile image click handler
            editProfileImageButton.setOnClickListener {
                // TODO: Implement image picker
                Toast.makeText(context, "Profile picture upload coming soon", Toast.LENGTH_SHORT).show()
            }

            // Phone number formatting
            phoneEditText.addTextChangedListener(object : TextWatcher {
                private var isFormatting = false
                private val maxLength = 10

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (isFormatting || s == null) return
                    isFormatting = true

                    // Remove all non-digits
                    val digits = s.toString().replace(Regex("[^\\d]"), "")

                    // Limit to 10 digits
                    val truncatedDigits = digits.take(maxLength)

                    // Format as (###) ###-####
                    val formatted = when {
                        truncatedDigits.length >= 7 -> {
                            "(${truncatedDigits.substring(0,3)}) ${truncatedDigits.substring(3,6)}-${truncatedDigits.substring(6)}"
                        }
                        truncatedDigits.length >= 4 -> {
                            "(${truncatedDigits.substring(0,3)}) ${truncatedDigits.substring(3)}"
                        }
                        truncatedDigits.length >= 1 -> {
                            "(${truncatedDigits}"
                        }
                        else -> ""
                    }

                    s.replace(0, s.length, formatted)
                    isFormatting = false
                }
            })

            // Save button click handler
            saveButton.setOnClickListener {
                saveProfile()
            }

            // Logout button click handler
            logoutButton.setOnClickListener {
                lifecycleScope.launch {
                    userRepository.signOut()
                    navigateToLogin()
                }
            }
        }
    }

    private fun loadUserProfile() {
        val currentUser = userRepository.getCurrentUser() ?: run {
            navigateToLogin()
            return
        }

        // Load user profile data
        lifecycleScope.launch {
            try {
                val profile = userRepository.getUserProfile(currentUser.uid)
                updateUI(profile)
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(profile: UserProfile) {
        binding.apply {
            nameEditText.setText(profile.name)
            emailEditText.setText(profile.email)
            phoneEditText.setText(profile.phone)

            // Update stats
            mealsClaimedText.text = profile.stats.mealsClaimedCount.toString()
            wasteSavedText.text = "${profile.stats.wasteSavedPounds} lbs"
        }
    }

    private fun saveProfile() {
        val currentUser = userRepository.getCurrentUser() ?: return

        val updatedProfile = UserProfile(
            id = currentUser.uid,
            name = binding.nameEditText.text.toString(),
            email = binding.emailEditText.text.toString(),
            phone = binding.phoneEditText.text.toString()
        )

        lifecycleScope.launch {
            try {
                userRepository.updateUserProfile(updatedProfile)
                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
