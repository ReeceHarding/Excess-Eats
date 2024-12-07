package com.yourcompany.excesseats.ui.discovery

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R

public class FoodPostDetailFragmentDirections private constructor() {
  public companion object {
    public fun actionFoodPostDetailFragmentToNavigationDiscover(): NavDirections =
        ActionOnlyNavDirections(R.id.action_foodPostDetailFragment_to_navigation_discover)
  }
}
