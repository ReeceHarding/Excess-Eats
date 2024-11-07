package com.exceseats.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.ChatRoom
import com.exceseats.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _chatRooms = MutableLiveData<List<ChatRoom>>()
    val chatRooms: LiveData<List<ChatRoom>> = _chatRooms

    init {
        loadChatRooms()
    }

    private fun loadChatRooms() {
        val currentUserId = auth.currentUser?.uid ?: return

        chatRepository.getChatRooms(currentUserId)
            .onEach { rooms ->
                _chatRooms.value = rooms
            }
            .catch { e ->
                Timber.e(e)
            }
            .launchIn(viewModelScope)
    }

    fun createChatRoom(otherUserId: String) {
        viewModelScope.launch {
            try {
                val currentUserId = auth.currentUser?.uid ?: return@launch
                chatRepository.createChatRoom(currentUserId, otherUserId)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
