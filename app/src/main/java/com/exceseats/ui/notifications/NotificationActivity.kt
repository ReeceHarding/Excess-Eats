package com.exceseats.ui.notifications

import android.os.Bundle
import androidx.activity.viewModels
import com.exceseats.ui.base.BaseActivity
import com.exceseats.databinding.ActivityNotificationsBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity<ActivityNotificationsBinding>() {

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationPagerAdapter

    override fun getViewBinding() = ActivityNotificationsBinding.inflate(layoutInflater)

    override fun setupViews() {
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        adapter = NotificationPagerAdapter(this)
        binding.viewPager.adapter = adapter
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "All Notifications"
                1 -> "Preferences"
                else -> ""
            }
        }.attach()
    }

    override fun observeViewModel() {
        viewModel.notifications.observe(this) { notifications ->
            adapter.submitList(notifications)
        }
    }
}
