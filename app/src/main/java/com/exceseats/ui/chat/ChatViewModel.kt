package com.exceseats.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exceseats.model.ChatMessage
import com.exceseats.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _messages = MutableLiveData<List<ChatMessage>>()
    val messages: LiveData<List<ChatMessage>> = _messages

    fun getCurrentUserId(): String = auth.currentUser?.uid
        ?: throw IllegalStateException("User not authenticated")

    fun loadMessages(roomId: String) {
        chatRepository.getMessages(roomId)
            .onEach { messages ->
                _messages.value = messages
                markMessagesAsRead(roomId)
            }
            .catch { /* Handle error */ }
            .launchIn(viewModelScope)
    }

    fun sendMessage(roomId: String, content: String) {
        viewModelScope.launch {
            val message = ChatMessage(
                messageId = UUID.randomUUID().toString(),
                senderId = getCurrentUserId(),
                content = content
            )
            chatRepository.sendMessage(roomId, message)
        }
    }

    private fun markMessagesAsRead(roomId: String) {
        viewModelScope.launch {
            chatRepository.markMessagesAsRead(roomId, getCurrentUserId())
        }
    }
}
