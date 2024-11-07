package com.exceseats.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exceseats.util.AnalyticsUtils
import javax.inject.Inject

abstract class AnalyticsActivity : AppCompatActivity() {

    @Inject
    lateinit var analytics: AnalyticsUtils

    abstract fun getScreenName(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logScreenView()
    }

    private fun logScreenView() {
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, getScreenName())
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, javaClass.simpleName)
            }
        )
    }
}
