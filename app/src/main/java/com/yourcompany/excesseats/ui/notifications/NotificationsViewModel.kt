package com.yourcompany.excesseats.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yourcompany.excesseats.models.Notification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NotificationsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")

                val notificationsRef = firestore.collection("user_notifications")
                    .whereEqualTo("userId", userId)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .orderBy("__name__", Query.Direction.DESCENDING)
                    .limit(50)

                val snapshot = notificationsRef.get().await()
                val notifications = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Notification::class.java)?.copy(id = doc.id)
                }

                _notifications.value = notifications
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load notifications"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                firestore.collection("user_notifications")
                    .document(notificationId)
                    .update("isRead", true)
                    .await()

                // Refresh notifications after marking as read
                loadNotifications()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to mark notification as read"
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                firestore.collection("user_notifications")
                    .document(notificationId)
                    .delete()
                    .await()

                // Refresh notifications after deletion
                loadNotifications()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to delete notification"
            }
        }
    }
}
