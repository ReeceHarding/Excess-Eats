package com.exceseats.ui.map

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class MapActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MapActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun mapFragment_isDisplayed() {
        onView(withId(R.id.mapFragment))
            .check(matches(isDisplayed()))
    }

    // Note: Testing Google Maps functionality requires more complex setup
    // and might require integration tests or manual testing
}
