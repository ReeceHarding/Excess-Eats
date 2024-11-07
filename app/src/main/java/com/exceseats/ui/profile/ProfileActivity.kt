package com.exceseats.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import com.exceseats.ui.base.BaseActivity
import com.exceseats.databinding.ActivityProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var adapter: ProfilePagerAdapter

    override fun getViewBinding() = ActivityProfileBinding.inflate(layoutInflater)

    override fun setupViews() {
        setupViewPager()
        setupTabLayout()
        observeViewModel()
    }

    private fun setupViewPager() {
        adapter = ProfilePagerAdapter(this)
        binding.viewPager.adapter = adapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Profile"
                1 -> "Stats"
                else -> ""
            }
        }.attach()
    }

    override fun observeViewModel() {
        viewModel.profileState.observe(this) { state ->
            when (state) {
                is ProfileState.Success -> {
                    binding.nameTextView.text = state.user.name
                    binding.roleTextView.text = if (state.isCaterer) "Caterer" else "Food Seeker"
                }
                is ProfileState.Error -> {
                    showToast(state.message)
                }
                else -> Unit
            }
        }
    }
}
