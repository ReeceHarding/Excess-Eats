package com.yourcompany.excesseats.data.repository

import com.yourcompany.excesseats.data.model.Item
import com.yourcompany.excesseats.data.model.ItemStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.*

class ItemRepository {
    // In-memory storage
    private val items = mutableMapOf<String, Item>()
    private val itemsFlow = MutableStateFlow<Map<String, Item>>(emptyMap())

    suspend fun createItem(item: Item): Result<Item> = try {
        val itemWithId = item.copy(id = UUID.randomUUID().toString())
        items[itemWithId.id] = itemWithId
        itemsFlow.emit(items.toMap())
        Result.success(itemWithId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateItem(item: Item): Result<Item> = try {
        items[item.id] = item
        itemsFlow.emit(items.toMap())
        Result.success(item)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getItem(itemId: String): Result<Item> = try {
        val item = items[itemId]
        if (item != null) {
            Result.success(item)
        } else {
            Result.failure(Exception("Item not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun deleteItem(itemId: String): Result<Unit> = try {
        items.remove(itemId)
        itemsFlow.emit(items.toMap())
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getNearbyItems(
        latitude: Double,
        longitude: Double,
        radiusInKm: Double
    ): Result<List<Item>> = try {
        val nearbyItems = items.values
            .filter { it.status == ItemStatus.AVAILABLE }
            .filter { item ->
                calculateDistance(
                    latitude, longitude,
                    item.latitude, item.longitude
                ) <= radiusInKm
            }
        Result.success(nearbyItems)
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun observeNearbyItems(
        latitude: Double,
        longitude: Double,
        radiusInKm: Double
    ): Flow<Result<List<Item>>> = itemsFlow.map { items ->
        try {
            val nearbyItems = items.values
                .filter { it.status == ItemStatus.AVAILABLE }
                .filter { item ->
                    calculateDistance(
                        latitude, longitude,
                        item.latitude, item.longitude
                    ) <= radiusInKm
                }
            Result.success(nearbyItems)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun claimItem(
        itemId: String,
        userId: String
    ): Result<Item> = try {
        val item = items[itemId]
        if (item == null) {
            Result.failure(Exception("Item not found"))
        } else if (item.status != ItemStatus.AVAILABLE) {
            Result.failure(Exception("Item is not available"))
        } else {
            val updatedItem = item.copy(
                status = ItemStatus.CLAIMED,
                claimedBy = userId,
                claimedAt = System.currentTimeMillis()
            )
            items[itemId] = updatedItem
            itemsFlow.emit(items.toMap())
            Result.success(updatedItem)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUserItems(userId: String): Result<List<Item>> = try {
        val userItems = items.values
            .filter { it.providerId == userId }
            .sortedByDescending { it.createdAt }
        Result.success(userItems)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getClaimedItems(userId: String): Result<List<Item>> = try {
        val claimedItems = items.values
            .filter { it.claimedBy == userId }
            .sortedByDescending { it.claimedAt }
        Result.success(claimedItems)
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
