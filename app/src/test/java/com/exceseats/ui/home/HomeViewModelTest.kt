package com.exceseats.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.exceseats.model.FoodPost
import com.exceseats.repository.FoodPostRepository
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

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository: FoodPostRepository = mockk()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadFoodPosts success updates state correctly`() {
        // Given
        val mockPosts = listOf(
            FoodPost(postId = "1", foodDescription = "Test Food"),
            FoodPost(postId = "2", foodDescription = "Another Food")
        )
        coEvery { repository.getActiveFoodPosts() } returns flowOf(mockPosts)

        // When
        viewModel.loadFoodPosts()

        // Then
        assert(viewModel.foodPosts.value is HomeState.Success)
        assert((viewModel.foodPosts.value as HomeState.Success).posts == mockPosts)
    }

    @Test
    fun `loadFoodPosts failure updates state correctly`() {
        // Given
        coEvery { repository.getActiveFoodPosts() } throws Exception("Failed to load posts")

        // When
        viewModel.loadFoodPosts()

        // Then
        assert(viewModel.foodPosts.value is HomeState.Error)
    }
}
