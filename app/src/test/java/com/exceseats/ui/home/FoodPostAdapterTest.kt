package com.exceseats.ui.home

import android.content.Context
import android.view.LayoutInflater
import com.exceseats.databinding.ItemFoodPostBinding
import com.exceseats.model.FoodPost
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.util.Date

class FoodPostAdapterTest {

    private lateinit var adapter: FoodPostAdapter
    private lateinit var mockOnItemClick: (FoodPost) -> Unit
    private lateinit var mockContext: Context
    private lateinit var mockBinding: ItemFoodPostBinding

    @Before
    fun setup() {
        mockOnItemClick = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)
        mockBinding = mockk(relaxed = true) {
            every { root.context } returns mockContext
        }

        adapter = FoodPostAdapter(mockOnItemClick)
    }

    @Test
    fun `adapter binds food post data correctly`() {
        // Given
        val foodPost = FoodPost(
            postId = "1",
            foodDescription = "Test Food",
            quantity = 5,
            timestamp = Date()
        )

        // When
        adapter.submitList(listOf(foodPost))

        // Then
        // Note: Due to the limitations of unit testing Android Views,
        // we can only verify that the list was submitted successfully
        assert(adapter.currentList.size == 1)
        assert(adapter.currentList[0] == foodPost)
    }

    @Test
    fun `clicking on item triggers callback`() {
        // Given
        val foodPost = FoodPost(
            postId = "1",
            foodDescription = "Test Food"
        )
        val viewHolder = FoodPostAdapter.FoodPostViewHolder(mockBinding)

        // When
        viewHolder.bind(foodPost)
        mockBinding.root.performClick()

        // Then
        verify { mockOnItemClick(foodPost) }
    }
}
