package com.yourcompany.excesseats.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.yourcompany.excesseats.data.model.Item
import com.yourcompany.excesseats.data.model.ItemStatus
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class ItemRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val itemsCollection = firestore.collection("items")

    suspend fun createItem(item: Item): Result<Item> = try {
        val itemWithId = item.copy(id = UUID.randomUUID().toString())
        itemsCollection.document(itemWithId.id).set(itemWithId).await()
        Result.success(itemWithId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateItem(item: Item): Result<Item> = try {
        itemsCollection.document(item.id).set(item).await()
        Result.success(item)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getItem(itemId: String): Result<Item> = try {
        val itemDoc = itemsCollection.document(itemId).get().await()
        val item = itemDoc.toObject(Item::class.java)
        if (item != null) {
            Result.success(item)
        } else {
            Result.failure(Exception("Item not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteItem(itemId: String): Result<Unit> = try {
        itemsCollection.document(itemId).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getNearbyItems(
        latitude: Double,
        longitude: Double,
        radiusInKm: Double
    ): Result<List<Item>> = try {
        // This is a simple implementation. In production, you'd want to use
        // geohashing or a more sophisticated spatial query system
        val items = itemsCollection
            .whereEqualTo("status", ItemStatus.AVAILABLE)
            .get()
            .await()
            .toObjects(Item::class.java)
            .filter { item ->
                calculateDistance(
                    latitude, longitude,
                    item.latitude, item.longitude
                ) <= radiusInKm
            }
        Result.success(items)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun observeNearbyItems(
        latitude: Double,
        longitude: Double,
        radiusInKm: Double
    ): Flow<Result<List<Item>>> = flow {
        try {
            val snapshot = itemsCollection
                .whereEqualTo("status", ItemStatus.AVAILABLE)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        throw e
                    }
                    val items = snapshot?.toObjects(Item::class.java)
                        ?.filter { item ->
                            calculateDistance(
                                latitude, longitude,
                                item.latitude, item.longitude
                            ) <= radiusInKm
                        } ?: emptyList()
                    emit(Result.success(items))
                }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun uploadItemImage(itemId: String, imageBytes: ByteArray): Result<String> = try {
        val imageRef = storage.reference.child("item_images/$itemId.jpg")
        imageRef.putBytes(imageBytes).await()
        val downloadUrl = imageRef.downloadUrl.await()
        Result.success(downloadUrl.toString())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun claimItem(
        itemId: String,
        userId: String
    ): Result<Item> = try {
        val itemDoc = itemsCollection.document(itemId)
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(itemDoc)
            val item = snapshot.toObject(Item::class.java)
            if (item == null) {
                throw Exception("Item not found")
            }
            if (item.status != ItemStatus.AVAILABLE) {
                throw Exception("Item is not available")
            }
            val updatedItem = item.copy(
                status = ItemStatus.CLAIMED,
                claimedBy = userId,
                claimedAt = System.currentTimeMillis()
            )
            transaction.set(itemDoc, updatedItem)
            updatedItem
        }.await()
        Result.success(itemDoc.get().await().toObject(Item::class.java)!!)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserItems(userId: String): Result<List<Item>> = try {
        val items = itemsCollection
            .whereEqualTo("providerId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Item::class.java)
        Result.success(items)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getClaimedItems(userId: String): Result<List<Item>> = try {
        val items = itemsCollection
            .whereEqualTo("claimedBy", userId)
            .orderBy("claimedAt", Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Item::class.java)
        Result.success(items)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371 // Earth's radius in kilometers

        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val latDiff = Math.toRadians(lat2 - lat1)
        val lonDiff = Math.toRadians(lon2 - lon1)

        val a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2)

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return r * c
    }
}
