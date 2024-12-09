package com.yourcompany.excesseats.ui.profile

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R

public class UserProfileFragmentDirections private constructor() {
  public companion object {
    public fun actionNavigationProfileToNavigationNotifications(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_profile_to_navigation_notifications)
  }
}
