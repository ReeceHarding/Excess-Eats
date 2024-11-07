package com.exceseats.repository

import com.exceseats.data.NetworkBoundResource
import com.exceseats.data.local.FoodPostDao
import com.exceseats.model.FoodPost
import com.exceseats.util.NetworkUtils
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FoodPostRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val foodPostDao: FoodPostDao,
    private val networkUtils: NetworkUtils
) {
    fun getActiveFoodPosts(): Flow<Resource<List<FoodPost>>> = networkBoundResource(
        query = {
            foodPostDao.getActiveFoodPosts().map { entities ->
                entities.map { it.toDomainModel() }
            }
        },
        fetch = {
            firestore.collection("foodPosts")
                .whereEqualTo("isActive", true)
                .get()
                .await()
                .toObjects(FoodPost::class.java)
        },
        saveFetchResult = { posts ->
            foodPostDao.updateFoodPosts(posts.map { it.toEntity() })
        },
        shouldFetch = { networkUtils.isNetworkAvailable() }
    )

    suspend fun createFoodPost(foodPost: FoodPost) {
        // Validate data before saving
        require(ValidationUtils.isValidFoodPost(foodPost)) {
            "Invalid food post data"
        }

        firestore.collection("foodPosts")
            .add(foodPost)
            .await()
    }
}
