package com.yourcompany.excesseats.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yourcompany.excesseats.data.model.Notification
import com.yourcompany.excesseats.data.model.NotificationType
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotificationRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val notificationsCollection = firestore.collection("notifications")

    fun getNotificationsForUser(userId: String): Flow<List<Notification>> = flow {
        try {
            val snapshot = notificationsCollection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val notifications = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Notification::class.java)
            }
            emit(notifications)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun createFoodPostNotification(
        userId: String,
        foodTitle: String,
        location: String
    ): Boolean {
        return createNotification(
            userId = userId,
            title = "New Food Available Nearby",
            message = "$foodTitle at $location",
            type = NotificationType.NEW_FOOD_NEARBY
        )
    }

    suspend fun createNotification(
        userId: String,
        title: String,
        message: String,
        type: NotificationType,
        relatedPostId: String? = null,
        data: Map<String, String>? = null
    ): Boolean {
        return try {
            val notification = Notification(
                userId = userId,
                title = title,
                message = message,
                type = type.name,
                relatedPostId = relatedPostId,
                data = data
            )

            notificationsCollection.add(notification).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun markAsRead(notificationId: String): Boolean {
        return try {
            notificationsCollection.document(notificationId)
                .update("isRead", true)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteNotification(notificationId: String): Boolean {
        return try {
            notificationsCollection.document(notificationId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUnreadCount(userId: String): Int {
        return try {
            val snapshot = notificationsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("isRead", false)
                .get()
                .await()
            snapshot.size()
        } catch (e: Exception) {
            0
        }
    }

    suspend fun getNotificationsByType(userId: String, type: NotificationType): List<Notification> {
        return try {
            val snapshot = notificationsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("type", type.name)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Notification::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
