package com.yourcompany.excesseats.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.yourcompany.excesseats.R

class NotificationPreferencesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notification_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPreferences(view)
    }

    private fun setupPreferences(view: View) {
        // Get switches
        val nearbyFoodSwitch = view.findViewById<android.widget.Switch>(R.id.nearbyFoodSwitch)
        val claimUpdatesSwitch = view.findViewById<android.widget.Switch>(R.id.claimUpdatesSwitch)
        val systemNotificationsSwitch = view.findViewById<android.widget.Switch>(R.id.systemNotificationsSwitch)

        // Set initial states
        nearbyFoodSwitch.isChecked = true
        claimUpdatesSwitch.isChecked = true
        systemNotificationsSwitch.isChecked = true

        // Set up listeners
        nearbyFoodSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "Nearby food notifications enabled" else "Nearby food notifications disabled")
        }

        claimUpdatesSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "Claim updates enabled" else "Claim updates disabled")
        }

        systemNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            showToast(if (isChecked) "System notifications enabled" else "System notifications disabled")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
