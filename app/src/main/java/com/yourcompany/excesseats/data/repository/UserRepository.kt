package com.yourcompany.excesseats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yourcompany.excesseats.data.model.User
import com.yourcompany.excesseats.data.model.Preference
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val usersCollection = firestore.collection("users")
    private val preferencesCollection = firestore.collection("preferences")

    suspend fun createUser(user: User): Result<User> = try {
        val userDoc = usersCollection.document(user.id)
        userDoc.set(user).await()
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUser(user: User): Result<User> = try {
        val userDoc = usersCollection.document(user.id)
        userDoc.set(user).await()
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUser(userId: String): Result<User> = try {
        val userDoc = usersCollection.document(userId).get().await()
        val user = userDoc.toObject(User::class.java)
        if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("User not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteUser(userId: String): Result<Unit> = try {
        usersCollection.document(userId).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUserPreferences(preferences: Preference): Result<Preference> = try {
        val prefDoc = preferencesCollection.document(preferences.userId)
        prefDoc.set(preferences).await()
        Result.success(preferences)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserPreferences(userId: String): Result<Preference> = try {
        val prefDoc = preferencesCollection.document(userId).get().await()
        val preferences = prefDoc.toObject(Preference::class.java)
        if (preferences != null) {
            Result.success(preferences)
        } else {
            Result.failure(Exception("Preferences not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun observeUser(userId: String): Flow<Result<User>> = flow {
        try {
            val snapshot = usersCollection.document(userId)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        throw e
                    }
                    val user = snapshot?.toObject(User::class.java)
                    if (user != null) {
                        emit(Result.success(user))
                    } else {
                        emit(Result.failure(Exception("User not found")))
                    }
                }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun uploadProfileImage(userId: String, imageBytes: ByteArray): Result<String> = try {
        val imageRef = storage.reference.child("profile_images/$userId.jpg")
        val uploadTask = imageRef.putBytes(imageBytes).await()
        val downloadUrl = imageRef.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUserLocation(
        userId: String,
        latitude: Double,
        longitude: Double,
        locationString: String
    ): Result<Unit> = try {
        usersCollection.document(userId).update(
            mapOf(
                "latitude" to latitude,
                "longitude" to longitude,
                "location" to locationString
            )
        ).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
