package com.exceseats.repository

import com.exceseats.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun createUser(user: User) = firestore
        .collection("users")
        .document(user.userId)
        .set(user)
        .await()

    suspend fun getUser(userId: String) = firestore
        .collection("users")
        .document(userId)
        .get()
        .await()
        .toObject(User::class.java)

    suspend fun updateUser(user: User) = firestore
        .collection("users")
        .document(user.userId)
        .set(user)
        .await()
}
