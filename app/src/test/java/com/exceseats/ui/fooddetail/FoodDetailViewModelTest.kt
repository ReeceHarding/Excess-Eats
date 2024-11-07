package com.exceseats.ui.fooddetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.exceseats.model.FoodPost
import com.exceseats.model.User
import com.exceseats.repository.FoodPostRepository
import com.exceseats.repository.UserRepository
import com.google.firebase.firestore.GeoPoint
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class FoodDetailViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val foodPostRepository: FoodPostRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private lateinit var viewModel: FoodDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FoodDetailViewModel(foodPostRepository, userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadFoodPostDetails success with provider updates state correctly`() {
        // Given
        val postId = "test-post-id"
        val providerId = "provider-id"
        val mockFoodPost = FoodPost(
            postId = postId,
            providerId = providerId,
            foodDescription = "Test Food",
            pickupLocation = GeoPoint(40.7128, -74.0060),
            timestamp = Date()
        )
        val mockProvider = User(
            userId = providerId,
            name = "Test Provider"
        )

        coEvery { foodPostRepository.getFoodPostById(postId) } returns mockFoodPost
        coEvery { userRepository.getUser(providerId) } returns mockProvider

        // When
        viewModel.loadFoodPostDetails(postId)

        // Then
        assert(viewModel.detailState.value is DetailState.Success)
        val successState = viewModel.detailState.value as DetailState.Success
        assert(successState.foodPost == mockFoodPost)
        assert(successState.provider == mockProvider)
    }

    @Test
    fun `loadFoodPostDetails with non-existent post updates state with error`() {
        // Given
        val postId = "non-existent-id"
        coEvery { foodPostRepository.getFoodPostById(postId) } returns null

        // When
        viewModel.loadFoodPostDetails(postId)

        // Then
        assert(viewModel.detailState.value is DetailState.Error)
        assert((viewModel.detailState.value as DetailState.Error).message == "Food post not found")
    }

    @Test
    fun `loadFoodPostDetails with repository error updates state with error`() {
        // Given
        val postId = "test-post-id"
        val errorMessage = "Failed to load food post"
        coEvery { foodPostRepository.getFoodPostById(postId) } throws Exception(errorMessage)

        // When
        viewModel.loadFoodPostDetails(postId)

        // Then
        assert(viewModel.detailState.value is DetailState.Error)
        assert((viewModel.detailState.value as DetailState.Error).message == errorMessage)
    }
}
