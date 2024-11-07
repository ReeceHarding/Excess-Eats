package com.exceseats.e2e

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exceseats.R
import com.exceseats.ui.auth.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FoodPostingFlowTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun completePostingFlow() {
        // Login
        onView(withId(R.id.emailEditText))
            .perform(typeText("test@example.com"), closeSoftKeyboard())
        onView(withId(R.id.passwordEditText))
            .perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        // Navigate to post food screen
        onView(withId(R.id.fabPostFood)).perform(click())

        // Create food post
        onView(withId(R.id.foodDescriptionEditText))
            .perform(typeText("Test Food Description"), closeSoftKeyboard())
        onView(withId(R.id.quantityEditText))
            .perform(typeText("5"), closeSoftKeyboard())
        onView(withId(R.id.postButton)).perform(click())

        // Verify post appears in list
        onView(withText("Test Food Description")).check(matches(isDisplayed()))
    }
}
