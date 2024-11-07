package com.exceseats.ui.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.exceseats.model.FoodPost
import com.exceseats.repository.FoodPostRepository
import com.google.firebase.firestore.GeoPoint
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.Date

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository: FoodPostRepository = mockk()
    private lateinit var viewModel: MapViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MapViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadFoodPosts success updates state with locations`() {
        // Given
        val mockPosts = listOf(
            FoodPost(
                postId = "1",
                foodDescription = "Test Food",
                pickupLocation = GeoPoint(40.7128, -74.0060),
                timestamp = Date()
            ),
            FoodPost(
                postId = "2",
                foodDescription = "Another Food",
                pickupLocation = GeoPoint(34.0522, -118.2437),
                timestamp = Date()
            )
        )
        coEvery { repository.getActiveFoodPosts() } returns flowOf(mockPosts)

        // When - loadFoodPosts is called in init block

        // Then
        assert(viewModel.mapState.value is MapState.Success)
        val successState = viewModel.mapState.value as MapState.Success
        assert(successState.posts == mockPosts)
        assert(successState.posts.all { it.pickupLocation != null })
    }

    @Test
    fun `loadFoodPosts failure updates state with error`() {
        // Given
        val errorMessage = "Failed to load food posts"
        coEvery { repository.getActiveFoodPosts() } throws Exception(errorMessage)

        // When - loadFoodPosts is called in init block

        // Then
        assert(viewModel.mapState.value is MapState.Error)
        assert((viewModel.mapState.value as MapState.Error).message == errorMessage)
    }
}
