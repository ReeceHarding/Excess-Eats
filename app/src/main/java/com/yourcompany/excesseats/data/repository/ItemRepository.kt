package com.yourcompany.excesseats.data.repository

import com.yourcompany.excesseats.data.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class ItemRepository private constructor() {
    // In-memory storage
    private val items = mutableMapOf<String, Item>()
    private val itemsFlow = MutableStateFlow<Map<String, Item>>(emptyMap())

    companion object {
        @Volatile
        private var instance: ItemRepository? = null

        fun getInstance(): ItemRepository {
            return instance ?: synchronized(this) {
                instance ?: ItemRepository().also { instance = it }
            }
        }
    }

    suspend fun createItem(item: Item): Result<Item> = try {
        items[item.id] = item
        itemsFlow.emit(items.toMap())
        Result.success(item)
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
