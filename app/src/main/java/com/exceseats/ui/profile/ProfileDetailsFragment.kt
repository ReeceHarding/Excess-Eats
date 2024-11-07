package com.exceseats.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.exceseats.databinding.FragmentProfileDetailsBinding
import com.exceseats.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {

    private var _binding: FragmentProfileDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()
            val location = binding.locationEditText.text.toString()
            val isCaterer = binding.catererModeSwitch.isChecked

            if (name.isBlank() || email.isBlank()) {
                requireContext().showToast("Please fill in all required fields")
                return@setOnClickListener
            }

            viewModel.updateProfile(name, email, phone, location, isCaterer)
        }
    }

    private fun observeViewModel() {
        viewModel.profileState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is ProfileState.Loading
            binding.contentGroup.isVisible = state is ProfileState.Success

            when (state) {
                is ProfileState.Success -> {
                    binding.apply {
                        nameEditText.setText(state.user.name)
                        emailEditText.setText(state.user.email)
                        phoneEditText.setText(state.user.preferences["phone"] as? String ?: "")
                        locationEditText.setText(state.user.preferences["location"] as? String ?: "")
                        catererModeSwitch.isChecked = state.isCaterer
                    }
                }
                is ProfileState.Error -> {
                    requireContext().showToast(state.message)
                }
                else -> Unit
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
