package com.exceseats.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.User
import com.exceseats.repository.UserRepository
import com.exceseats.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    private val _statsState = MutableLiveData<StatsState>()
    val statsState: LiveData<StatsState> = _statsState

    init {
        loadProfile()
        loadStats()
    }

    fun updateProfile(
        name: String,
        email: String,
        phone: String,
        location: String,
        isCaterer: Boolean
    ) {
        viewModelScope.launch {
            try {
                _profileState.value = ProfileState.Loading
                val currentState = _profileState.value
                if (currentState is ProfileState.Success) {
                    val updatedUser = currentState.user.copy(
                        name = name,
                        email = email,
                        preferences = mapOf(
                            "phone" to phone,
                            "location" to location,
                            "userType" to if (isCaterer) Constants.USER_TYPE_PROVIDER else Constants.USER_TYPE_CONSUMER
                        )
                    )
                    userRepository.updateUser(updatedUser)
                    _profileState.value = ProfileState.Success(updatedUser, isCaterer)
                }
            } catch (e: Exception) {
                Timber.e(e)
                _profileState.value = ProfileState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = ProfileState.Loading
                val user = userRepository.getCurrentUser()
                user?.let {
                    val isCaterer = it.userType == Constants.USER_TYPE_PROVIDER
                    _profileState.value = ProfileState.Success(it, isCaterer)
                } ?: run {
                    _profileState.value = ProfileState.Error("User not found")
                }
            } catch (e: Exception) {
                Timber.e(e)
                _profileState.value = ProfileState.Error(e.message ?: "Failed to load profile")
            }
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                _statsState.value = StatsState.Loading
                val stats = userRepository.getUserStats()
                _statsState.value = StatsState.Success(stats)
            } catch (e: Exception) {
                Timber.e(e)
                _statsState.value = StatsState.Error(e.message ?: "Failed to load stats")
            }
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val user: User, val isCaterer: Boolean) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

sealed class StatsState {
    object Loading : StatsState()
    data class Success(val stats: UserStats) : StatsState()
    data class Error(val message: String) : StatsState()
}

data class UserStats(
    val mealsClaimed: Int = 0,
    val wasteReduced: Int = 0,
    val foodPosts: Int = 0
)
