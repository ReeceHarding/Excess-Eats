package com.yourcompany.excesseats.ui.discovery

import android.os.Bundle
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R
import kotlin.Float
import kotlin.Int
import kotlin.String

public class ItemDiscoveryFragmentDirections private constructor() {
  private data class ActionNavigationDiscoverToFoodPostDetailFragment(
    public val postId: String,
    public val title: String,
    public val latitude: Float,
    public val longitude: Float,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_discover_to_foodPostDetailFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putString("postId", this.postId)
        result.putString("title", this.title)
        result.putFloat("latitude", this.latitude)
        result.putFloat("longitude", this.longitude)
        return result
      }
  }

  public companion object {
    public fun actionNavigationDiscoverToFoodPostDetailFragment(
      postId: String,
      title: String,
      latitude: Float,
      longitude: Float,
    ): NavDirections = ActionNavigationDiscoverToFoodPostDetailFragment(postId, title, latitude,
        longitude)
  }
}
