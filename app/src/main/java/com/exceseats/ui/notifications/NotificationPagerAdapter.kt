package com.exceseats.ui.notifications

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class NotificationPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NotificationListFragment()
            1 -> NotificationPreferencesFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
