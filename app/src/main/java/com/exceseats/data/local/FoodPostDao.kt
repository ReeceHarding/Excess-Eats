package com.exceseats.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodPostDao {
    @Query("SELECT * FROM food_posts WHERE isActive = 1")
    fun getActiveFoodPosts(): Flow<List<FoodPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodPosts(posts: List<FoodPostEntity>)

    @Query("DELETE FROM food_posts")
    suspend fun clearAllPosts()

    @Transaction
    suspend fun updateFoodPosts(posts: List<FoodPostEntity>) {
        clearAllPosts()
        insertFoodPosts(posts)
    }
}
