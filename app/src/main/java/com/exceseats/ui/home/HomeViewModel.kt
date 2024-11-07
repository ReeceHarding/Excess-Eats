package com.exceseats.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.FoodPost
import com.exceseats.repository.FoodPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodPostRepository: FoodPostRepository
) : ViewModel() {

    private val _foodPosts = MutableLiveData<HomeState>()
    val foodPosts: LiveData<HomeState> = _foodPosts

    init {
        loadFoodPosts()
    }

    fun loadFoodPosts() {
        _foodPosts.value = HomeState.Loading
        foodPostRepository.getActiveFoodPosts()
            .onEach { posts ->
                _foodPosts.value = HomeState.Success(posts)
            }
            .catch { e ->
                Timber.e(e)
                _foodPosts.value = HomeState.Error(e.message ?: "Failed to load food posts")
            }
            .launchIn(viewModelScope)
    }
}

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val posts: List<FoodPost>) : HomeState()
    data class Error(val message: String) : HomeState()
}
