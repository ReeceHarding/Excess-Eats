package com.yourcompany.excesseats.ui.profile

import android.os.Bundle
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R
import kotlin.Float
import kotlin.Int
import kotlin.String

public class ProfileFragmentDirections private constructor() {
  private data class ActionNavigationProfileToPostDetails(
    public val postId: String,
    public val title: String,
    public val latitude: Float,
    public val longitude: Float,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_navigation_profile_to_postDetails

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
    public fun actionNavigationProfileToPostDetails(
      postId: String,
      title: String,
      latitude: Float,
      longitude: Float,
    ): NavDirections = ActionNavigationProfileToPostDetails(postId, title, latitude, longitude)
  }
}
