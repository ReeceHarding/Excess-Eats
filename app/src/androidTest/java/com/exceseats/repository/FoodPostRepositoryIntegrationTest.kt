package com.exceseats.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.exceseats.model.FoodPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import java.util.Date

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FoodPostRepositoryIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var firestore: FirebaseFirestore

    private lateinit var repository: FoodPostRepository

    @Before
    fun setup() {
        hiltRule.inject()
        repository = FoodPostRepository(firestore)
    }

    @Test
    fun createAndRetrieveFoodPost() = runBlocking {
        // Given
        val foodPost = FoodPost(
            postId = "test-post",
            foodDescription = "Test Food",
            quantity = 5,
            pickupLocation = GeoPoint(40.7128, -74.0060),
            timestamp = Date()
        )

        // When
        repository.createFoodPost(foodPost)
        val retrievedPosts = repository.getActiveFoodPosts().first()

        // Then
        assert(retrievedPosts.any { it.postId == foodPost.postId })
    }
}
