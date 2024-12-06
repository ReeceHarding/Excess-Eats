package com.yourcompany.excesseats.data.repository

import android.content.Context
import com.yourcompany.excesseats.data.local.NotificationDatabase
import com.yourcompany.excesseats.data.local.NotificationEntity
import com.yourcompany.excesseats.data.model.Notification
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotificationRepository private constructor(context: Context) {
    private val notificationDao = NotificationDatabase.getDatabase(context).notificationDao()

    fun getAllNotifications(): Flow<List<Notification>> {
        return notificationDao.getAllNotifications().map { entities ->
            entities.map { entity ->
                Notification(
                    id = entity.id,
                    title = entity.title,
                    message = entity.message,
                    postId = entity.postId,
                    timestamp = entity.timestamp,
                    isRead = entity.isRead
                )
            }
        }
    }

    suspend fun addNotification(notification: Notification) {
        val entity = NotificationEntity(
            id = notification.id,
            title = notification.title,
            message = notification.message,
            postId = notification.postId,
            timestamp = notification.timestamp,
            isRead = notification.isRead
        )
        notificationDao.insertNotification(entity)
    }

    suspend fun markAsRead(notificationId: Long) {
        notificationDao.markAsRead(notificationId)
    }

    companion object {
        @Volatile
        private var instance: NotificationRepository? = null

        fun getInstance(context: Context): NotificationRepository {
            return instance ?: synchronized(this) {
                instance ?: NotificationRepository(context).also { instance = it }
            }
        }
    }
} 