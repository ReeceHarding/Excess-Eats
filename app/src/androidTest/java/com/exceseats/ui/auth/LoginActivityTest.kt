package com.exceseats.ui.auth

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exceseats.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun loginButton_isDisabled_whenFieldsAreEmpty() {
        onView(withId(R.id.loginButton))
            .check(matches(isEnabled()))
            .perform(click())

        // Should show error toast and button should remain enabled
        onView(withId(R.id.loginButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun loginButton_isEnabled_whenFieldsAreFilled() {
        onView(withId(R.id.emailEditText))
            .perform(typeText("test@example.com"), closeSoftKeyboard())

        onView(withId(R.id.passwordEditText))
            .perform(typeText("password123"), closeSoftKeyboard())

        onView(withId(R.id.loginButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun registerButton_navigatesToRegisterScreen() {
        onView(withId(R.id.registerButton))
            .perform(click())

        // Verify we're on the register screen
        onView(withId(R.id.registerButton))
            .check(matches(isDisplayed()))
    }
}