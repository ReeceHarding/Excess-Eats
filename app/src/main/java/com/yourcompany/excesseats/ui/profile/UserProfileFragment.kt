package com.yourcompany.excesseats.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.yourcompany.excesseats.auth.LoginActivity
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
        updateAuthState()
    }

    private fun setupViews() {
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                userRepository.signOut()
                updateAuthState()
            }
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun updateAuthState() {
        val currentUser = userRepository.getCurrentUser()
        if (currentUser != null) {
            binding.loggedInLayout.visibility = View.VISIBLE
            binding.loggedOutLayout.visibility = View.GONE
        } else {
            binding.loggedInLayout.visibility = View.GONE
            binding.loggedOutLayout.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        updateAuthState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
