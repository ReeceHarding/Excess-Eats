package com.exceseats.ui.chat

import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceseats.databinding.ActivityChatListBinding
import com.exceseats.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListActivity : BaseActivity<ActivityChatListBinding>() {

    private val viewModel: ChatListViewModel by viewModels()
    private lateinit var adapter: ChatRoomAdapter

    override fun getViewBinding() = ActivityChatListBinding.inflate(layoutInflater)

    override fun setupViews() {
        adapter = ChatRoomAdapter { room ->
            startActivity(ChatActivity.createIntent(this, room.roomId))
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatListActivity)
            adapter = this@ChatListActivity.adapter
        }
    }

    override fun observeViewModel() {
        viewModel.chatRooms.observe(this) { rooms ->
            adapter.submitList(rooms)
            binding.emptyView.isVisible = rooms.isEmpty()
        }
    }
}
