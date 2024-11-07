package com.exceseats.navigation

import android.content.Context
import android.content.Intent
import com.exceseats.ui.auth.LoginActivity
import com.exceseats.ui.auth.RegisterActivity
import com.exceseats.ui.chat.ChatActivity
import com.exceseats.ui.fooddetail.FoodDetailActivity
import com.exceseats.ui.home.HomeActivity
import com.exceseats.ui.map.MapActivity
import com.exceseats.ui.notifications.NotificationActivity
import com.exceseats.ui.postfood.PostFoodActivity
import com.exceseats.ui.profile.ProfileActivity
import com.exceseats.ui.settings.SettingsActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    fun navigateToLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    fun navigateToHome(context: Context) {
        context.startActivity(Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    fun navigateToRegister(context: Context) {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    fun navigateToFoodDetail(context: Context, postId: String) {
        context.startActivity(FoodDetailActivity.createIntent(context, postId))
    }

    fun navigateToMap(context: Context) {
        context.startActivity(Intent(context, MapActivity::class.java))
    }

    fun navigateToPostFood(context: Context) {
        context.startActivity(Intent(context, PostFoodActivity::class.java))
    }

    fun navigateToProfile(context: Context) {
        context.startActivity(Intent(context, ProfileActivity::class.java))
    }

    fun navigateToSettings(context: Context) {
        context.startActivity(Intent(context, SettingsActivity::class.java))
    }

    fun navigateToNotifications(context: Context) {
        context.startActivity(Intent(context, NotificationActivity::class.java))
    }

    fun navigateToChat(context: Context, roomId: String) {
        context.startActivity(ChatActivity.createIntent(context, roomId))
    }
}
