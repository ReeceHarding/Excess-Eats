package com.yourcompany.excesseats.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourcompany.excesseats.data.repository.NotificationRepository
import com.yourcompany.excesseats.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotificationAdapter
    private lateinit var repository: NotificationRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = NotificationRepository.getInstance(requireContext())
        setupRecyclerView()
        loadNotifications()
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter { notification ->
            // Handle notification click
            notification.postId?.let { postId ->
                findNavController().navigate(
                    NotificationsFragmentDirections.actionNotificationsToFoodPostDetail(
                        postId = postId,
                        title = notification.title,
                        latitude = 0f, // You'll need to get these from the post
                        longitude = 0f
                    )
                )
                // Mark as read
                lifecycleScope.launch {
                    repository.markAsRead(notification.id)
                }
            }
        }

        binding.notificationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = this@NotificationsFragment.adapter
        }
    }

    private fun loadNotifications() {
        lifecycleScope.launch {
            repository.getAllNotifications().collectLatest { notifications ->
                adapter.submitList(notifications)
                binding.emptyView.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
