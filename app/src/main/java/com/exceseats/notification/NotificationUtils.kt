package com.exceseats.notification

import android.content.Context
import com.exceseats.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtils @Inject constructor(
    @ApplicationContext private val context: Context,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) {

    companion object {
        private const val PREFS_NAME = "notification_prefs"
        private const val KEY_FCM_TOKEN = "fcm_token"

        fun updateFcmToken(context: Context, token: String) {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString(KEY_FCM_TOKEN, token).apply()

            // Update token in Firestore
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(currentUser.uid)
                    .update("fcmToken", token)
            }
        }
    }

    fun sendNotificationToNearbyUsers(
        foodPostId: String,
        title: String,
        message: String,
        location: com.google.firebase.firestore.GeoPoint
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Get all users within notification radius
                val users = firestore.collection("users")
                    .whereEqualTo("userType", "consumer")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { it.toObject(com.exceseats.model.User::class.java) }

                // Filter users based on their notification radius preference and location
                users.forEach { user ->
                    user.location?.let { userLocation ->
                        val distance = calculateDistance(
                            userLocation.latitude,
                            userLocation.longitude,
                            location.latitude,
                            location.longitude
                        )

                        val userRadius = (user.preferences["notificationRadius"] as? Number)?.toInt()
                            ?: com.exceseats.util.Constants.DEFAULT_NOTIFICATION_RADIUS

                        if (distance <= userRadius) {
                            // Send FCM notification to this user
                            sendFcmNotification(
                                user.userId,
                                foodPostId,
                                title,
                                message
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                timber.log.Timber.e(e)
            }
        }
    }

    private fun calculateDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in kilometers

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)

        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return r * c
    }

    private suspend fun sendFcmNotification(
        userId: String,
        foodPostId: String,
        title: String,
        message: String
    ) {
        try {
            val user = userRepository.getUser(userId)
            val fcmToken = user?.preferences?.get("fcmToken") as? String

            if (fcmToken != null) {
                // Send FCM notification using your server or Firebase Cloud Functions
                // This is just a placeholder - you'll need to implement the actual FCM sending logic
                // through your backend server or Firebase Cloud Functions
            }
        } catch (e: Exception) {
            timber.log.Timber.e(e)
        }
    }
}
