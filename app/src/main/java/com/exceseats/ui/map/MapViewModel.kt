package com.exceseats.ui.map

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
class MapViewModel @Inject constructor(
    private val foodPostRepository: FoodPostRepository
) : ViewModel() {

    private val _mapState = MutableLiveData<MapState>()
    val mapState: LiveData<MapState> = _mapState

    init {
        loadFoodPosts()
    }

    private fun loadFoodPosts() {
        foodPostRepository.getActiveFoodPosts()
            .onEach { posts ->
                _mapState.value = MapState.Success(posts)
            }
            .catch { e ->
                Timber.e(e)
                _mapState.value = MapState.Error(e.message ?: "Failed to load food posts")
            }
            .launchIn(viewModelScope)
    }
}

sealed class MapState {
    object Loading : MapState()
    data class Success(val posts: List<FoodPost>) : MapState()
    data class Error(val message: String) : MapState()
}
