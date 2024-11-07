package com.exceseats.ui.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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
class HomeActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(HomeActivity::class.java)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun recyclerView_isDisplayed() {
        onView(withId(R.id.foodPostsRecyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun fabMap_opensMapScreen() {
        onView(withId(R.id.fabMap))
            .perform(click())

        // Verify we're on the map screen
        onView(withId(R.id.mapFragment))
            .check(matches(isDisplayed()))
    }

    @Test
    fun fabPostFood_opensPostFoodScreen() {
        onView(withId(R.id.fabPostFood))
            .perform(click())

        // Verify we're on the post food screen
        onView(withId(R.id.foodDescriptionEditText))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickingFoodPost_opensFoodDetailScreen() {
        // Assuming there's at least one item in the list
        onView(withId(R.id.foodPostsRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<FoodPostAdapter.FoodPostViewHolder>(0, click()))

        // Verify we're on the food detail screen
        onView(withId(R.id.foodDescriptionTextView))
            .check(matches(isDisplayed()))
    }
}
