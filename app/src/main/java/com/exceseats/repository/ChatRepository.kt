package com.exceseats.repository

import com.exceseats.model.ChatMessage
import com.exceseats.model.ChatRoom
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    fun getChatRooms(userId: String): Flow<List<ChatRoom>> = callbackFlow {
        val subscription = firestore.collection("chatRooms")
            .whereArrayContains("participants", userId)
            .orderBy("lastMessageTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val rooms = snapshot?.toObjects(ChatRoom::class.java) ?: emptyList()
                trySend(rooms)
            }

        awaitClose { subscription.remove() }
    }

    fun getMessages(roomId: String): Flow<List<ChatMessage>> = callbackFlow {
        val subscription = firestore.collection("chatRooms")
            .document(roomId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.toObjects(ChatMessage::class.java) ?: emptyList()
                trySend(messages)
            }

        awaitClose { subscription.remove() }
    }

    suspend fun sendMessage(roomId: String, message: ChatMessage) {
        firestore.collection("chatRooms")
            .document(roomId)
            .collection("messages")
            .add(message)
            .await()

        // Update chat room's last message
        firestore.collection("chatRooms")
            .document(roomId)
            .update(
                mapOf(
                    "lastMessage" to message.content,
                    "lastMessageTime" to message.timestamp
                )
            )
            .await()
    }

    suspend fun createChatRoom(userId1: String, userId2: String): String {
        val room = ChatRoom(
            roomId = "$userId1-$userId2",
            participants = listOf(userId1, userId2)
        )

        firestore.collection("chatRooms")
            .document(room.roomId)
            .set(room)
            .await()

        return room.roomId
    }

    suspend fun markMessagesAsRead(roomId: String, userId: String) {
        firestore.collection("chatRooms")
            .document(roomId)
            .collection("messages")
            .whereEqualTo("receiverId", userId)
            .whereEqualTo("read", false)
            .get()
            .await()
            .documents
            .forEach { doc ->
                doc.reference.update("read", true)
            }
    }
}
