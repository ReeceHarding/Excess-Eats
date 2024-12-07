/*
package com.yourcompany.excesseats.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourcompany.excesseats.data.model.Notification
import com.yourcompany.excesseats.data.model.NotificationType
import com.yourcompany.excesseats.data.repository.NotificationRepository
import com.yourcompany.excesseats.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotificationAdapter
    private val repository = NotificationRepository.getInstance()

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
        setupRecyclerView()
        loadNotifications()
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter { notification ->
            handleNotificationClick(notification)
        }

        binding.notificationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificationsFragment.adapter
        }
    }

    private fun loadNotifications() {
        viewLifecycleOwner.lifecycleScope.launch {
            repository.getNotifications().collectLatest { result ->
                result.onSuccess { notifications ->
                    adapter.submitList(notifications)
                    binding.emptyView.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
                    binding.notificationsRecyclerView.visibility = if (notifications.isEmpty()) View.GONE else View.VISIBLE
                }.onFailure { exception ->
                    Toast.makeText(
                        requireContext(),
                        "Error loading notifications: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun handleNotificationClick(notification: Notification) {
        viewLifecycleOwner.lifecycleScope.launch {
            repository.markAsRead(notification.id)

            when (notification.type) {
                NotificationType.FOOD_CLAIMED, NotificationType.NEW_FOOD_NEARBY -> {
                    notification.relatedPostId?.let { postId ->
                        // Navigate to food post detail
                        // Note: You'll need to implement this navigation
                    }
                }
                else -> {
                    // Handle other notification types
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/
