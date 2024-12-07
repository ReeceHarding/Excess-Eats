package com.yourcompany.excesseats.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yourcompany.excesseats.data.model.User
import com.yourcompany.excesseats.data.model.Preference
import com.yourcompany.excesseats.data.model.UserProfile
import com.yourcompany.excesseats.data.model.UserStats
import com.yourcompany.excesseats.data.model.FoodPost
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepository private constructor() {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val usersRef = database.getReference("users")
    private val preferencesRef = database.getReference("preferences")

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
        }
    }

    suspend fun createUser(user: User): Result<User> {
        return try {
            // Create authentication account
            val authResult = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("Failed to create authentication account"))

            // Create user profile in database (without password)
            val userProfile = user.copy(
                id = firebaseUser.uid,
                password = "" // Don't store password in database
            )
            usersRef.child(firebaseUser.uid).setValue(userProfile).await()

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
                ?: return Result.failure(Exception("Failed to sign in"))

            val snapshot = usersRef.child(firebaseUser.uid).get().await()
            val user = snapshot.getValue<User>()
                ?: return Result.failure(Exception("User profile not found"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun updateUser(user: User): Result<User> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("No authenticated user"))

            // Update authentication profile if needed
            if (user.email != currentUser.email) {
                currentUser.updateEmail(user.email).await()
            }

            // Update user profile in database (without password)
            val userProfile = user.copy(password = "")
            usersRef.child(currentUser.uid).setValue(userProfile).await()

            Result.success(userProfile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<User> {
        return try {
            val snapshot = usersRef.child(userId).get().await()
            val user = snapshot.getValue<User>()
                ?: return Result.failure(Exception("User not found"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserByEmail(email: String): Result<User> {
        return try {
            val snapshot = usersRef.orderByChild("email").equalTo(email).get().await()
            val user = snapshot.children.firstOrNull()?.getValue<User>()
                ?: return Result.failure(Exception("User not found"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("No authenticated user"))

            if (currentUser.uid != userId) {
                return Result.failure(Exception("Cannot delete other users"))
            }

            // Delete from authentication
            currentUser.delete().await()

            // Delete from database
            usersRef.child(userId).removeValue().await()
            preferencesRef.child(userId).removeValue().await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserPreferences(preferences: Preference): Result<Preference> {
        return try {
            val currentUser = auth.currentUser
                ?: return Result.failure(Exception("No authenticated user"))

            preferencesRef.child(currentUser.uid).setValue(preferences).await()
            Result.success(preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserPreferences(userId: String): Result<Preference> {
        return try {
            val snapshot = preferencesRef.child(userId).get().await()
            val preferences = snapshot.getValue<Preference>()
                ?: return Result.failure(Exception("Preferences not found"))

            Result.success(preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun observeUser(userId: String): Flow<Result<User>> {
        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue<User>()
                    if (user != null) {
                        trySend(Result.success(user))
                    } else {
                        trySend(Result.failure(Exception("User not found")))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(Result.failure(error.toException()))
                }
            }

            usersRef.child(userId).addValueEventListener(listener)
            awaitClose { usersRef.child(userId).removeEventListener(listener) }
        }
    }

    suspend fun getUserProfile(userId: String): UserProfile {
        return try {
            val snapshot = usersRef.child(userId).get().await()
            val user = snapshot.getValue<User>()
                ?: throw Exception("User not found")

            // Get user stats from a separate node
            val statsSnapshot = usersRef.child(userId).child("stats").get().await()
            val stats = if (statsSnapshot.exists()) {
                UserStats(
                    mealsClaimedCount = statsSnapshot.child("mealsClaimedCount").getValue<Int>() ?: 0,
                    wasteSavedPounds = statsSnapshot.child("wasteSavedPounds").getValue<Double>() ?: 0.0
                )
            } else {
                UserStats()
            }

            // Convert User to UserProfile
            UserProfile(
                id = user.id,
                name = user.name,
                email = user.email,
                phone = user.phone,
                stats = stats
            )
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        try {
            val currentUser = auth.currentUser
                ?: throw Exception("No authenticated user")

            // Update only the profile fields in the user document
            val updates = hashMapOf<String, Any>(
                "name" to profile.name,
                "email" to profile.email,
                "phone" to profile.phone
            )

            // Update authentication email if it changed
            if (profile.email != currentUser.email) {
                currentUser.updateEmail(profile.email).await()
            }

            // Update profile in database
            usersRef.child(currentUser.uid).updateChildren(updates).await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateUserStats(userId: String, foodPost: FoodPost) {
        try {
            val statsRef = usersRef.child(userId).child("stats")
            val currentStats = statsRef.get().await()

            val currentMealsClaimed = currentStats.child("mealsClaimedCount").getValue<Int?>() ?: 0
            val currentWasteSaved = currentStats.child("wasteSavedPounds").getValue<Double?>() ?: 0.0

            val updates = hashMapOf<String, Any>(
                "mealsClaimedCount" to (currentMealsClaimed + foodPost.remainingQuantity),
                "wasteSavedPounds" to (currentWasteSaved + foodPost.getEstimatedWeight())
            )

            statsRef.updateChildren(updates).await()
        } catch (e: Exception) {
            throw e
        }
    }
}
