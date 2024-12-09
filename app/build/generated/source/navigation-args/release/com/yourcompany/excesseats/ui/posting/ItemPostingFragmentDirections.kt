package com.yourcompany.excesseats.ui.posting

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R

public class ItemPostingFragmentDirections private constructor() {
  public companion object {
    public fun actionNavigationPostToNavigationNotifications(): NavDirections =
        ActionOnlyNavDirections(R.id.action_navigation_post_to_navigation_notifications)
  }
}
