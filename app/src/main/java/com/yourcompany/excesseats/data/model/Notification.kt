package com.yourcompany.excesseats.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Notification(
    @DocumentId
    val id: String = "",

    @PropertyName("userId")
    val userId: String = "",

    @PropertyName("title")
    val title: String = "",

    @PropertyName("message")
    val message: String = "",

    @ServerTimestamp
    @PropertyName("timestamp")
    val timestamp: Date = Date(),

    @PropertyName("type")
    val type: String = NotificationType.SYSTEM_MESSAGE.name,

    @PropertyName("isRead")
    var isRead: Boolean = false,

    @PropertyName("relatedPostId")
    val relatedPostId: String? = null,

    @PropertyName("data")
    val data: Map<String, String>? = null
)
