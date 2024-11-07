package com.exceseats.ui.fooddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.FoodPost
import com.exceseats.model.User
import com.exceseats.repository.FoodPostRepository
import com.exceseats.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodPostRepository: FoodPostRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _detailState = MutableLiveData<DetailState>()
    val detailState: LiveData<DetailState> = _detailState

    fun loadFoodPostDetails(postId: String) {
        viewModelScope.launch {
            try {
                _detailState.value = DetailState.Loading
                val foodPost = foodPostRepository.getFoodPostById(postId)
                foodPost?.let {
                    val provider = userRepository.getUser(it.providerId)
                    _detailState.value = DetailState.Success(it, provider)
                } ?: run {
                    _detailState.value = DetailState.Error("Food post not found")
                }
            } catch (e: Exception) {
                Timber.e(e)
                _detailState.value = DetailState.Error(e.message ?: "Failed to load food post details")
            }
        }
    }
}

sealed class DetailState {
    object Loading : DetailState()
    data class Success(
        val foodPost: FoodPost,
        val provider: User?
    ) : DetailState()
    data class Error(val message: String) : DetailState()
}
