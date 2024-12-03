package com.yourcompany.excesseats.data.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    companion object {
        private const val BASE_URL = "https://api.yourcompany.com/v1/"
        const val GEOCODING_API = "${BASE_URL}geocoding/"
        const val NOTIFICATIONS_API = "${BASE_URL}notifications/"
        const val ANALYTICS_API = "${BASE_URL}analytics/"
    }

    // Geocoding endpoints
    @GET("${GEOCODING_API}reverse")
    suspend fun reverseGeocode(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<GeocodingResponse>

    @GET("${GEOCODING_API}forward")
    suspend fun forwardGeocode(
        @Query("address") address: String
    ): Response<GeocodingResponse>

    // Notifications endpoints
    @POST("${NOTIFICATIONS_API}send")
    suspend fun sendNotification(
        @Body notification: NotificationRequest
    ): Response<NotificationResponse>

    @GET("${NOTIFICATIONS_API}user/{userId}")
    suspend fun getUserNotifications(
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<List<NotificationResponse>>

    // Analytics endpoints
    @POST("${ANALYTICS_API}event")
    suspend fun logAnalyticsEvent(
        @Body event: AnalyticsEvent
    ): Response<Unit>

    @GET("${ANALYTICS_API}user/{userId}/stats")
    suspend fun getUserStats(
        @Path("userId") userId: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<UserStats>
}

// Data classes for API responses and requests
data class GeocodingResponse(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String
)

data class NotificationRequest(
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val data: Map<String, String>
)

data class NotificationResponse(
    val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    val data: Map<String, String>,
    val read: Boolean,
    val createdAt: Long
)

data class AnalyticsEvent(
    val userId: String,
    val eventType: String,
    val eventData: Map<String, Any>,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserStats(
    val userId: String,
    val itemsPosted: Int,
    val itemsClaimed: Int,
    val totalInteractions: Int,
    val averageResponseTime: Long,
    val successfulTransactions: Int,
    val failedTransactions: Int,
    val rating: Float
)
