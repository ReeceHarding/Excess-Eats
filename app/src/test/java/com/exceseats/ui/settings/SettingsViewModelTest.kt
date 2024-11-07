package com.exceseats.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.exceseats.model.User
import com.exceseats.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.every
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

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val auth: FirebaseAuth = mockk(relaxed = true)
    private val repository: UserRepository = mockk()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val mockUser = mockk<FirebaseUser>()
        every { auth.currentUser } returns mockUser
        every { mockUser.uid } returns "test-uid"

        viewModel = SettingsViewModel(auth, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `loadUserProfile success updates state correctly`() {
        // Given
        val mockUser = User(userId = "test-uid", name = "Test User")
        coEvery { repository.getUser("test-uid") } returns mockUser

        // When - loadUserProfile is called in init block

        // Then
        assert(viewModel.settingsState.value is SettingsState.Success)
        assert((viewModel.settingsState.value as SettingsState.Success).user == mockUser)
    }

    @Test
    fun `updateProfile success updates state correctly`() {
        // Given
        val initialUser = User(userId = "test-uid", name = "Test User")
        coEvery { repository.getUser("test-uid") } returns initialUser
        coEvery { repository.updateUser(any()) } returns Unit

        // When
        viewModel.updateProfile("New Name", 10)

        // Then
        assert(viewModel.settingsState.value is SettingsState.Success)
        assert((viewModel.settingsState.value as SettingsState.Success).user.name == "New Name")
    }

    @Test
    fun `logout updates state correctly`() {
        // When
        viewModel.logout()

        // Then
        assert(viewModel.settingsState.value is SettingsState.LoggedOut)
    }
}
