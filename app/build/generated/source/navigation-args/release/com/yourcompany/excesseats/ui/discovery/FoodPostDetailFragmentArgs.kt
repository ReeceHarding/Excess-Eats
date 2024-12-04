package com.yourcompany.excesseats.ui.discovery

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Float
import kotlin.String
import kotlin.jvm.JvmStatic

public data class FoodPostDetailFragmentArgs(
  public val postId: String,
  public val title: String,
  public val latitude: Float,
  public val longitude: Float,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("postId", this.postId)
    result.putString("title", this.title)
    result.putFloat("latitude", this.latitude)
    result.putFloat("longitude", this.longitude)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("postId", this.postId)
    result.set("title", this.title)
    result.set("latitude", this.latitude)
    result.set("longitude", this.longitude)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): FoodPostDetailFragmentArgs {
      bundle.setClassLoader(FoodPostDetailFragmentArgs::class.java.classLoader)
      val __postId : String?
      if (bundle.containsKey("postId")) {
        __postId = bundle.getString("postId")
        if (__postId == null) {
          throw IllegalArgumentException("Argument \"postId\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"postId\" is missing and does not have an android:defaultValue")
      }
      val __title : String?
      if (bundle.containsKey("title")) {
        __title = bundle.getString("title")
        if (__title == null) {
          throw IllegalArgumentException("Argument \"title\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"title\" is missing and does not have an android:defaultValue")
      }
      val __latitude : Float
      if (bundle.containsKey("latitude")) {
        __latitude = bundle.getFloat("latitude")
      } else {
        throw IllegalArgumentException("Required argument \"latitude\" is missing and does not have an android:defaultValue")
      }
      val __longitude : Float
      if (bundle.containsKey("longitude")) {
        __longitude = bundle.getFloat("longitude")
      } else {
        throw IllegalArgumentException("Required argument \"longitude\" is missing and does not have an android:defaultValue")
      }
      return FoodPostDetailFragmentArgs(__postId, __title, __latitude, __longitude)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle):
        FoodPostDetailFragmentArgs {
      val __postId : String?
      if (savedStateHandle.contains("postId")) {
        __postId = savedStateHandle["postId"]
        if (__postId == null) {
          throw IllegalArgumentException("Argument \"postId\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"postId\" is missing and does not have an android:defaultValue")
      }
      val __title : String?
      if (savedStateHandle.contains("title")) {
        __title = savedStateHandle["title"]
        if (__title == null) {
          throw IllegalArgumentException("Argument \"title\" is marked as non-null but was passed a null value")
        }
      } else {
        throw IllegalArgumentException("Required argument \"title\" is missing and does not have an android:defaultValue")
      }
      val __latitude : Float?
      if (savedStateHandle.contains("latitude")) {
        __latitude = savedStateHandle["latitude"]
        if (__latitude == null) {
          throw IllegalArgumentException("Argument \"latitude\" of type float does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"latitude\" is missing and does not have an android:defaultValue")
      }
      val __longitude : Float?
      if (savedStateHandle.contains("longitude")) {
        __longitude = savedStateHandle["longitude"]
        if (__longitude == null) {
          throw IllegalArgumentException("Argument \"longitude\" of type float does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"longitude\" is missing and does not have an android:defaultValue")
      }
      return FoodPostDetailFragmentArgs(__postId, __title, __latitude, __longitude)
    }
  }
}
