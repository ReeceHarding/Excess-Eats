/*
package com.yourcompany.excesseats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yourcompany.excesseats.data.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NotificationRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getNotifications(): Flow<Result<List<Notification>>> = flow {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val notifications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Notification::class.java)
            }
            emit(Result.success(notifications))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun markAsRead(notificationId: String) {
        try {
            val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
            firestore.collection("users")
                .document(userId)
                .collection("notifications")
                .document(notificationId)
                .update("isRead", true)
                .await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    companion object {
        @Volatile
        private var instance: NotificationRepository? = null

        fun getInstance(): NotificationRepository {
            return instance ?: synchronized(this) {
                instance ?: NotificationRepository().also { instance = it }
            }
        }
    }
}
*/
