package com.exceseats.ui.settings

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
class SettingsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _settingsState = MutableLiveData<SettingsState>()
    val settingsState: LiveData<SettingsState> = _settingsState

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _settingsState.value = SettingsState.Loading
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not authenticated")

                val user = userRepository.getUser(userId)
                user?.let {
                    _settingsState.value = SettingsState.Success(it)
                } ?: run {
                    _settingsState.value = SettingsState.Error("User profile not found")
                }
            } catch (e: Exception) {
                Timber.e(e)
                _settingsState.value = SettingsState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    fun updateProfile(name: String, notificationRadius: Int) {
        viewModelScope.launch {
            try {
                _settingsState.value = SettingsState.Loading
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not authenticated")

                val currentState = _settingsState.value
                if (currentState is SettingsState.Success) {
                    val updatedUser = currentState.user.copy(
                        name = name,
                        preferences = mapOf("notificationRadius" to notificationRadius)
                    )
                    userRepository.updateUser(updatedUser)
                    _settingsState.value = SettingsState.Success(updatedUser)
                }
            } catch (e: Exception) {
                Timber.e(e)
                _settingsState.value = SettingsState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _settingsState.value = SettingsState.LoggedOut
    }
}

sealed class SettingsState {
    object Loading : SettingsState()
    data class Success(val user: User) : SettingsState()
    data class Error(val message: String) : SettingsState()
    object LoggedOut : SettingsState()
}
