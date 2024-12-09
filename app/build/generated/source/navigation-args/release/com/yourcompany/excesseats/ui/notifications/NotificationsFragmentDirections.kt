package com.yourcompany.excesseats.ui.notifications

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R

public class NotificationsFragmentDirections private constructor() {
  public companion object {
    public fun actionNavigationNotificationsToNavigationDiscover(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_notifications_to_navigation_discover)

    public fun actionNavigationNotificationsToNavigationPost(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_notifications_to_navigation_post)

    public fun actionNavigationNotificationsToNavigationProfile(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_notifications_to_navigation_profile)
  }
}
