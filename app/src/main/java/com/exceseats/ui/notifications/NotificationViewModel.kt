package com.exceseats.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _notificationState = MutableLiveData<NotificationState>()
    val notificationState: LiveData<NotificationState> = _notificationState

    private val _preferencesState = MutableLiveData<NotificationPreferencesState>()
    val preferencesState: LiveData<NotificationPreferencesState> = _preferencesState

    init {
        loadNotifications()
        loadPreferences()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _notificationState.value = NotificationState.Loading
                val notifications = notificationRepository.getNotifications()
                _notificationState.value = NotificationState.Success(notifications)
            } catch (e: Exception) {
                Timber.e(e)
                _notificationState.value = NotificationState.Error(e.message ?: "Failed to load notifications")
            }
        }
    }

    fun updatePreferences(
        nearbyFood: Boolean,
        claimUpdates: Boolean,
        systemNotifications: Boolean
    ) {
        viewModelScope.launch {
            try {
                _preferencesState.value = NotificationPreferencesState.Loading
                notificationRepository.updatePreferences(
                    nearbyFood = nearbyFood,
                    claimUpdates = claimUpdates,
                    systemNotifications = systemNotifications
                )
                _preferencesState.value = NotificationPreferencesState.Success
            } catch (e: Exception) {
                Timber.e(e)
                _preferencesState.value = NotificationPreferencesState.Error(e.message ?: "Failed to update preferences")
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                notificationRepository.markAsRead(notificationId)
                loadNotifications() // Reload to reflect changes
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}

sealed class NotificationState {
    object Loading : NotificationState()
    data class Success(val notifications: List<Notification>) : NotificationState()
    data class Error(val message: String) : NotificationState()
}

sealed class NotificationPreferencesState {
    object Loading : NotificationPreferencesState()
    object Success : NotificationPreferencesState()
    data class Error(val message: String) : NotificationPreferencesState()
}
