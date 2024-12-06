package com.yourcompany.excesseats.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val message: String,
    val postId: String?,
    val timestamp: Long,
    val isRead: Boolean
)

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :notificationId")
    suspend fun markAsRead(notificationId: Long)
}

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        fun getDatabase(context: Context): NotificationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotificationDatabase::class.java,
                    "notification_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 