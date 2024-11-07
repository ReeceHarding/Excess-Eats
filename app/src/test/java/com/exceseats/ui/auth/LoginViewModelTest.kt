package com.exceseats.ui.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val auth: FirebaseAuth = mockk(relaxed = true)
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(auth)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `login success updates state correctly`() {
        // Given
        val email = "test@example.com"
        val password = "password"
        val mockUser = mockk<FirebaseUser>()
        coEvery { auth.signInWithEmailAndPassword(email, password).await() } returns mockk {
            every { user } returns mockUser
        }

        // When
        viewModel.login(email, password)

        // Then
        assert(viewModel.loginState.value is LoginState.Success)
    }

    @Test
    fun `login failure updates state correctly`() {
        // Given
        val email = "test@example.com"
        val password = "password"
        coEvery { auth.signInWithEmailAndPassword(email, password).await() } throws Exception("Auth failed")

        // When
        viewModel.login(email, password)

        // Then
        assert(viewModel.loginState.value is LoginState.Error)
    }
}
