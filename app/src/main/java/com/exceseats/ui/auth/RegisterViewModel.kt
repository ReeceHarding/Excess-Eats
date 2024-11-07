package com.exceseats.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.User
import com.exceseats.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> = _registerState

    fun register(name: String, email: String, password: String, userType: String) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading
                val result = auth.createUserWithEmailAndPassword(email, password).await()

                result.user?.let { firebaseUser ->
                    val user = User(
                        userId = firebaseUser.uid,
                        name = name,
                        email = email,
                        userType = userType
                    )
                    userRepository.createUser(user)
                    _registerState.value = RegisterState.Success
                }
            } catch (e: Exception) {
                Timber.e(e)
                _registerState.value = RegisterState.Error(e.message ?: "Registration failed")
            }
        }
    }
}

sealed class RegisterState {
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}
