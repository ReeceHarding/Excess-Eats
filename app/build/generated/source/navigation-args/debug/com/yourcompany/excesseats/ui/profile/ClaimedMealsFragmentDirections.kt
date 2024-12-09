package com.yourcompany.excesseats.ui.profile

import android.os.Bundle
import androidx.navigation.NavDirections
import com.yourcompany.excesseats.R
import kotlin.Float
import kotlin.Int
import kotlin.String

public class ClaimedMealsFragmentDirections private constructor() {
  private data class ActionClaimedMealsFragmentToFoodPostDetailFragment(
    public val postId: String,
    public val title: String,
    public val latitude: Float,
    public val longitude: Float,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_claimedMealsFragment_to_foodPostDetailFragment

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
    public fun actionClaimedMealsFragmentToFoodPostDetailFragment(
      postId: String,
      title: String,
      latitude: Float,
      longitude: Float,
    ): NavDirections = ActionClaimedMealsFragmentToFoodPostDetailFragment(postId, title, latitude,
        longitude)
  }
}
