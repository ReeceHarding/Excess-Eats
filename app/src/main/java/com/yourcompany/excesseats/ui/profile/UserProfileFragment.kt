package com.yourcompany.excesseats.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.yourcompany.excesseats.R
import com.yourcompany.excesseats.auth.LoginActivity
import com.yourcompany.excesseats.data.model.UserProfile
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.databinding.FragmentUserProfileBinding
import com.yourcompany.excesseats.utils.Logger
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository.getInstance()
    private val foodPostRepository = FoodPostRepository.getInstance()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { handleImageSelection(it) }
    }

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
            // Profile image click handlers
            profileImage.setOnClickListener {
                openImagePicker()
            }

            editProfileImageButton.setOnClickListener {
                openImagePicker()
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

                    // Format the phone number
                    val formatted = when {
                        digits.length >= 10 -> {
                            val areaCode = digits.substring(0, 3)
                            val prefix = digits.substring(3, 6)
                            val number = digits.substring(6, minOf(10, digits.length))
                            "($areaCode) $prefix-$number"
                        }
                        digits.length >= 6 -> {
                            val areaCode = digits.substring(0, 3)
                            val prefix = digits.substring(3, digits.length)
                            "($areaCode) $prefix"
                        }
                        digits.length >= 3 -> {
                            val areaCode = digits.substring(0, digits.length)
                            "($areaCode"
                        }
                        else -> digits
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

    private fun openImagePicker() {
        pickImage.launch("image/*")
    }

    private fun handleImageSelection(uri: Uri) {
        val currentUser = userRepository.getCurrentUser() ?: return

        lifecycleScope.launch {
            try {
                binding.profileImage.isEnabled = false
                binding.editProfileImageButton.isEnabled = false

                val result = userRepository.uploadProfileImage(currentUser.uid, uri)
                result.onSuccess { imageUrl ->
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .circleCrop()
                        .placeholder(R.drawable.ic_person)
                        .into(binding.profileImage)
                    Toast.makeText(context, "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
                }.onFailure { exception ->
                    Toast.makeText(context, "Failed to upload image: ${exception.message}", Toast.LENGTH_SHORT).show()
                    Logger.e("Failed to upload profile image", exception)
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error uploading image: ${e.message}", Toast.LENGTH_SHORT).show()
                Logger.e("Error uploading profile image", e)
            } finally {
                binding.profileImage.isEnabled = true
                binding.editProfileImageButton.isEnabled = true
            }
        }
    }

    private fun loadUserProfile() {
        val currentUser = userRepository.getCurrentUser() ?: run {
            navigateToLogin()
            return
        }

        lifecycleScope.launch {
            try {
                // Update claim count first
                foodPostRepository.updateUserClaimCount(currentUser.uid)
                
                // Then load profile which will show updated count
                val profile = userRepository.getUserProfile(currentUser.uid)
                updateUI(profile)
            } catch (e: Exception) {
                Toast.makeText(context, "Error loading profile: ${e.message}", Toast.LENGTH_SHORT).show()
                Logger.e("Error loading profile", e)
            }
        }
    }

    private fun updateUI(profile: UserProfile) {
        binding.apply {
            nameEditText.setText(profile.name)
            emailEditText.setText(profile.email)
            phoneEditText.setText(profile.phone)

            // Load profile image if available
            val currentUser = userRepository.getCurrentUser()
            if (currentUser != null) {
                lifecycleScope.launch {
                    try {
                        val user = userRepository.getUser(currentUser.uid).getOrNull()
                        user?.profileImageUrl?.let { imageUrl ->
                            Glide.with(requireContext())
                                .load(imageUrl)
                                .circleCrop()
                                .placeholder(R.drawable.ic_person)
                                .into(profileImage)
                        }
                    } catch (e: Exception) {
                        Logger.e("Error loading profile image", e)
                    }
                }
            }

            // Update stats
            mealsClaimedText.text = profile.stats.mealsClaimedCount.toString()
            wasteSavedText.text = String.format("%.1f lbs", profile.stats.wasteSavedPounds)
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
                Logger.e("Error updating profile", e)
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
