package com.yourcompany.excesseats.data.repository

import com.yourcompany.excesseats.data.model.User
import com.yourcompany.excesseats.data.model.Preference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class UserRepository {
    // In-memory storage
    private val users = mutableMapOf<String, User>()
    private val preferences = mutableMapOf<String, Preference>()
    private val userFlow = MutableStateFlow<Map<String, User>>(emptyMap())

    suspend fun createUser(user: User): Result<User> = try {
        users[user.id] = user
        userFlow.emit(users.toMap())
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUser(user: User): Result<User> = try {
        users[user.id] = user
        userFlow.emit(users.toMap())
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUser(userId: String): Result<User> = try {
        val user = users[userId]
        if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("User not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteUser(userId: String): Result<Unit> = try {
        users.remove(userId)
        userFlow.emit(users.toMap())
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateUserPreferences(preferences: Preference): Result<Preference> = try {
        this.preferences[preferences.userId] = preferences
        Result.success(preferences)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserPreferences(userId: String): Result<Preference> = try {
        val prefs = preferences[userId]
        if (prefs != null) {
            Result.success(prefs)
        } else {
            Result.failure(Exception("Preferences not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun observeUser(userId: String): Flow<Result<User>> = userFlow.map { users ->
        val user = users[userId]
        if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("User not found"))
        }
    }

    suspend fun updateUserLocation(
        userId: String,
        latitude: Double,
        longitude: Double,
        locationString: String
    ): Result<Unit> = try {
        val user = users[userId]?.copy(
            latitude = latitude,
            longitude = longitude,
            location = locationString
        )
        if (user != null) {
            users[userId] = user
            userFlow.emit(users.toMap())
            Result.success(Unit)
        } else {
            Result.failure(Exception("User not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
