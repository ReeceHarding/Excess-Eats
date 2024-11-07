package com.exceseats.ui.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceseats.databinding.ActivityChatBinding
import com.exceseats.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatActivity : BaseActivity<ActivityChatBinding>() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatMessageAdapter

    override fun getViewBinding() = ActivityChatBinding.inflate(layoutInflater)

    override fun setupViews() {
        val roomId = intent.getStringExtra(EXTRA_ROOM_ID)
            ?: throw IllegalArgumentException("Room ID is required")

        adapter = ChatMessageAdapter(viewModel.getCurrentUserId())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = this@ChatActivity.adapter
        }

        binding.sendButton.setOnClickListener {
            val message = binding.messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(roomId, message)
                binding.messageInput.setText("")
            }
        }

        viewModel.loadMessages(roomId)
    }

    override fun observeViewModel() {
        viewModel.messages.observe(this) { messages ->
            adapter.submitList(messages) {
                binding.recyclerView.scrollToPosition(messages.size - 1)
            }
        }
    }

    companion object {
        private const val EXTRA_ROOM_ID = "extra_room_id"

        fun createIntent(context: Context, roomId: String): Intent {
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_ROOM_ID, roomId)
            }
        }
    }
}
