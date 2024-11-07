package com.exceseats.ui.postfood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.FoodPost
import com.exceseats.repository.FoodPostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID
import android.net.Uri

@HiltViewModel
class PostFoodViewModel @Inject constructor(
    private val foodPostRepository: FoodPostRepository,
    private val auth: FirebaseAuth,
    private val imageUploadUtils: ImageUploadUtils
) : ViewModel() {

    private val _postState = MutableLiveData<PostFoodState>()
    val postState: LiveData<PostFoodState> = _postState

    private var selectedImageUri: Uri? = null

    fun setSelectedImage(uri: Uri) {
        selectedImageUri = uri
        // Notify UI to show selected image preview
        _postState.value = PostFoodState.ImageSelected(uri)
    }

    fun createFoodPost(
        description: String,
        foodType: String,
        quantity: Int,
        location: GeoPoint
    ) {
        viewModelScope.launch {
            try {
                _postState.value = PostFoodState.Loading

                // Upload image if selected
                val imageUrl = selectedImageUri?.let { uri ->
                    imageUploadUtils.uploadImage(uri, "food_posts")
                }

                val foodPost = FoodPost(
                    postId = UUID.randomUUID().toString(),
                    providerId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated"),
                    foodDescription = description,
                    foodType = foodType,
                    quantity = quantity,
                    pickupLocation = location,
                    isActive = true,
                    imageUrl = imageUrl
                )

                foodPostRepository.createFoodPost(foodPost)
                _postState.value = PostFoodState.Success
            } catch (e: Exception) {
                Timber.e(e)
                _postState.value = PostFoodState.Error(e.message ?: "Failed to create food post")
            }
        }
    }
}

sealed class PostFoodState {
    object Loading : PostFoodState()
    object Success : PostFoodState()
    data class Error(val message: String) : PostFoodState()
    data class ImageSelected(val uri: Uri) : PostFoodState()
}
