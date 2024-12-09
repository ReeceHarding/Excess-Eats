package com.yourcompany.excesseats.data.repository

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yourcompany.excesseats.data.model.ClaimInfo
import com.yourcompany.excesseats.data.model.FoodPost
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.android.gms.maps.model.LatLng
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class FoodPostRepository {
    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")

    companion object {
        private const val TAG = "FoodPostRepository"
        private var instance: FoodPostRepository? = null

        fun getInstance(): FoodPostRepository {
            return instance ?: synchronized(this) {
                instance ?: FoodPostRepository().also { instance = it }
            }
        }
    }

    suspend fun claimPost(postId: String, userId: String): Result<Unit> = try {
        val post = getFoodPost(postId).getOrNull() ?: throw Exception("Post not found")

        if (post.isClaimedByUser(userId)) {
            throw Exception("You have already claimed this post")
        }

        if (post.getRemainingServings() <= 0) {
            throw Exception("No servings remaining")
        }

        val claimInfo = ClaimInfo(
            userId = userId,
            claimedAt = System.currentTimeMillis(),
            servingsClaimed = 1
        )

        val updatedClaims = post.claimedByUsers.toMutableMap()
        updatedClaims[userId] = claimInfo

        // Update post claims
        postsRef.child(postId).child("claimedByUsers").setValue(updatedClaims).await()

        // Update user's claimed meals count
        val userRef = database.getReference("users").child(userId).child("stats")
        val currentCount = userRef.child("mealsClaimedCount").get().await().getValue(Int::class.java) ?: 0
        userRef.child("mealsClaimedCount").setValue(currentCount + 1).await()

        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "Error claiming post", e)
        Result.failure(e)
    }

    suspend fun unclaimPost(postId: String, userId: String): Result<Unit> = try {
        val post = getFoodPost(postId).getOrNull() ?: throw Exception("Post not found")

        if (!post.isClaimedByUser(userId)) {
            throw Exception("You haven't claimed this post")
        }

        val updatedClaims = post.claimedByUsers.toMutableMap()
        updatedClaims.remove(userId)

        postsRef.child(postId).child("claimedByUsers").setValue(updatedClaims).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "Error unclaiming post", e)
        Result.failure(e)
    }

    suspend fun getFoodPost(postId: String): Result<FoodPost> = try {
        val snapshot = postsRef.child(postId).get().await()
        val post = snapshot.getValue<FoodPost>()
        if (post != null) {
            Result.success(post)
        } else {
            Result.failure(Exception("Post not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getAllPosts(): Flow<Result<List<FoodPost>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = snapshot.children
                    .mapNotNull { it.getValue<FoodPost>() }
                    // Filter out posts with no servings left
                    .filter { post -> post.getRemainingServings() > 0 }
                trySend(Result.success(posts))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        postsRef.addValueEventListener(listener)
        awaitClose { postsRef.removeEventListener(listener) }
    }

    fun getNearbyPosts(location: LatLng, radiusKm: Double): Flow<Result<List<FoodPost>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = snapshot.children.mapNotNull { it.getValue<FoodPost>() }
                val nearbyPosts = posts.filter { post ->
                    calculateDistance(
                        location.latitude, location.longitude,
                        post.latitude, post.longitude
                    ) <= radiusKm
                }
                trySend(Result.success(nearbyPosts))
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        postsRef.addValueEventListener(listener)
        awaitClose { postsRef.removeEventListener(listener) }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth's radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    suspend fun migrateExistingPosts(): Result<Unit> = try {
        val snapshot = postsRef.get().await()
        val updates = mutableMapOf<String, Any>()
        val userUpdates = mutableMapOf<String, Any>()

        snapshot.children.forEach { postSnapshot ->
            val post = postSnapshot.getValue<FoodPost>()
            if (post != null && post.claimedByUsers.isEmpty() && post.claimedBy != null) {
                // Migrate old claimed posts
                val updatedPost = post.copy(
                    claimedByUsers = mapOf(post.claimedBy to ClaimInfo(
                        userId = post.claimedBy,
                        claimedAt = post.claimedAt ?: System.currentTimeMillis(),
                        servingsClaimed = 1
                    ))
                )
                updates[postSnapshot.key!!] = updatedPost

                // Update user's claimed meals count for old claims
                val userStatsRef = database.getReference("users")
                    .child(post.claimedBy)
                    .child("stats")
                    .child("mealsClaimedCount")
                
                try {
                    val currentCount = userStatsRef.get().await().getValue(Int::class.java) ?: 0
                    userStatsRef.setValue(currentCount + 1).await()
                } catch (e: Exception) {
                    Log.e(TAG, "Error updating user stats during migration", e)
                }
            }
        }

        if (updates.isNotEmpty()) {
            postsRef.updateChildren(updates).await()
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Log.e(TAG, "Error migrating posts", e)
        Result.failure(e)
    }

    fun getClaimedPosts(userId: String): Flow<Result<List<FoodPost>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val posts = snapshot.children
                        .mapNotNull { it.getValue<FoodPost>() }
                        .filter { post -> post.isClaimedByUser(userId) }
                    trySend(Result.success(posts))
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        postsRef.addValueEventListener(listener)
        awaitClose { postsRef.removeEventListener(listener) }
    }

    suspend fun createPost(post: FoodPost): Result<FoodPost> = try {
        postsRef.child(post.id).setValue(post).await()
        Result.success(post)
    } catch (e: Exception) {
        Log.e(TAG, "Error creating post", e)
        Result.failure(e)
    }

    suspend fun updateUserClaimCount(userId: String) {
        try {
            // Get all posts
            val snapshot = postsRef.get().await()
            var totalClaims = 0

            // Count all posts claimed by this user
            snapshot.children.forEach { postSnapshot ->
                val post = postSnapshot.getValue<FoodPost>()
                if (post != null) {
                    // Check both old and new claim systems
                    if (post.claimedBy == userId || post.isClaimedByUser(userId)) {
                        totalClaims++
                    }
                }
            }

            // Update user's stats with total count
            val userStatsRef = database.getReference("users")
                .child(userId)
                .child("stats")
                .child("mealsClaimedCount")
            
            userStatsRef.setValue(totalClaims).await()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error updating user claim count", e)
        }
    }
}
