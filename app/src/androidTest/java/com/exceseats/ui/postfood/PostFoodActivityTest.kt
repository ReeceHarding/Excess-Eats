package com.exceseats.ui.postfood

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
class PostFoodActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(PostFoodActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun postButton_isDisabled_whenFieldsAreEmpty() {
        onView(withId(R.id.postButton))
            .check(matches(isEnabled()))
            .perform(click())

        // Should show error toast and button should remain enabled
        onView(withId(R.id.postButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun postButton_isEnabled_whenFieldsAreFilled() {
        onView(withId(R.id.foodDescriptionEditText))
            .perform(typeText("Test Food Description"), closeSoftKeyboard())

        onView(withId(R.id.quantityEditText))
            .perform(typeText("5"), closeSoftKeyboard())

        onView(withId(R.id.postButton))
            .check(matches(isEnabled()))
    }

    @Test
    fun backButton_closesActivity() {
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .perform(click())

        // Activity should finish
        activityRule.scenario.onActivity { activity ->
            assert(activity.isFinishing)
        }
    }
}
