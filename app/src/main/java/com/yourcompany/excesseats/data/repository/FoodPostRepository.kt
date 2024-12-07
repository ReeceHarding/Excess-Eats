package com.yourcompany.excesseats.data.repository

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.yourcompany.excesseats.data.model.FoodPost
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import android.util.Log

class FoodPostRepository private constructor() {
    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")
    private val userRepository = UserRepository.getInstance()

    companion object {
        @Volatile
        private var instance: FoodPostRepository? = null
        internal const val TAG = "FoodPostRepository"

        fun getInstance(): FoodPostRepository {
            return instance ?: synchronized(this) {
                instance ?: FoodPostRepository().also { instance = it }
            }
        }
    }

    suspend fun createPost(post: FoodPost): Result<FoodPost> = try {
        postsRef.child(post.id).setValue(post).await()
        Result.success(post)
    } catch (e: Exception) {
        Log.e(TAG, "Error creating post", e)
        Result.failure(e)
    }

    suspend fun updatePost(post: FoodPost): Result<FoodPost> = try {
        postsRef.child(post.id).setValue(post).await()
        Result.success(post)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deletePost(postId: String): Result<Unit> = try {
        postsRef.child(postId).removeValue().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun getAllPosts(): Flow<Result<List<FoodPost>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("FoodPostRepository", "Got ${snapshot.childrenCount} posts from Firebase")
                val posts = snapshot.children.mapNotNull { it.getValue<FoodPost>() }
                    .filter { it.remainingQuantity > 0 }
                Log.d("FoodPostRepository", "Mapped to ${posts.size} FoodPost objects")
                posts.forEach { post ->
                    Log.d("FoodPostRepository", "Post: ${post.title} at ${post.latitude}, ${post.longitude}")
                }
                trySend(Result.success(posts))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FoodPostRepository", "Error loading posts", error.toException())
                trySend(Result.failure(error.toException()))
            }
        }

        postsRef.addValueEventListener(listener)
        awaitClose { postsRef.removeEventListener(listener) }
    }

    fun getPostsByUser(userId: String): Flow<Result<List<FoodPost>>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val posts = snapshot.children
                    .mapNotNull { it.getValue<FoodPost>() }
                    .filter { it.userId == userId && it.remainingQuantity > 0 }
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
                    .filter { it.remainingQuantity > 0 }
                trySend(Result.success(posts))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FoodPostRepository", "Error loading posts", error.toException())
                trySend(Result.failure(error.toException()))
            }
        }

        postsRef.addValueEventListener(listener)
        awaitClose { postsRef.removeEventListener(listener) }
    }

    fun getFoodPost(postId: String): Flow<Result<FoodPost>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val post = snapshot.getValue<FoodPost>()
                if (post != null) {
                    trySend(Result.success(post))
                } else {
                    trySend(Result.failure(NoSuchElementException("Food post not found")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }

        postsRef.child(postId).addValueEventListener(listener)
        awaitClose { postsRef.child(postId).removeEventListener(listener) }
    }

    suspend fun claimFoodPost(postId: String, userId: String): Result<Unit> {
        return try {
            val snapshot = postsRef.child(postId).get().await()
            val post = snapshot.getValue<FoodPost>() ?: throw NoSuchElementException("Post not found")

            if (post.remainingQuantity <= 0) {
                return Result.failure(IllegalStateException("Food post fully claimed"))
            }

            val newQuantity = post.remainingQuantity - 1
            val updates = if (newQuantity > 0) {
                mapOf(
                    "remainingQuantity" to newQuantity,
                    "claimedBy" to userId,
                    "claimedAt" to System.currentTimeMillis()
                )
            } else {
                mapOf(
                    "remainingQuantity" to 0,
                    "isClaimed" to true,
                    "claimedBy" to userId,
                    "claimedAt" to System.currentTimeMillis()
                )
            }

            // Update the post first
            postsRef.child(postId).updateChildren(updates).await()

            // Then update user stats
            userRepository.updateUserStats(userId, post.copy(remainingQuantity = 1)) // Only count 1 serving for stats

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error claiming food post", e)
            Result.failure(e)
        }
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0 // Earth's radius in kilometers

        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val lonDiff = Math.toRadians(lon2 - lon1)
        val latDiff = Math.toRadians(lat2 - lat1)

        val a = sin(latDiff / 2) * sin(latDiff / 2) +
                cos(lat1Rad) * cos(lat2Rad) *
                sin(lonDiff / 2) * sin(lonDiff / 2)

        val c = 2 * Math.atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    suspend fun migrateExistingPosts() = try {
        val snapshot = postsRef.get().await()
        val updates = mutableMapOf<String, Any>()

        snapshot.children.forEach { postSnapshot ->
            val post = postSnapshot.getValue<FoodPost>()
            if (post != null) {
                // Parse the quantity string to get initial quantity
                // Example: "10 servings" -> 10
                val quantityStr = post.quantity.split(" ")[0]
                val initialQuantity = quantityStr.toIntOrNull() ?: 1

                // Only update if remainingQuantity isn't set or if post is claimed
                if (postSnapshot.child("remainingQuantity").value == null) {
                    val remainingQuantity = if (post.isClaimed) 0 else initialQuantity
                    updates["${post.id}/remainingQuantity"] = remainingQuantity
                }
            }
        }

        // Perform all updates in a single batch
        if (updates.isNotEmpty()) {
            postsRef.updateChildren(updates).await()
            Log.d("FoodPostRepository", "Successfully migrated ${updates.size} posts")
        }

        Result.success(Unit)
    } catch (e: Exception) {
        Log.e("FoodPostRepository", "Error migrating posts", e)
        Result.failure(e)
    }
}
