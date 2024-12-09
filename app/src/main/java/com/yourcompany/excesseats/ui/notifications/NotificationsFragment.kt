package com.yourcompany.excesseats.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.yourcompany.excesseats.databinding.FragmentNotificationsBinding
import com.yourcompany.excesseats.models.Notification
import com.yourcompany.excesseats.ui.notifications.adapter.NotificationsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var adapter: NotificationsAdapter

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

        // Check Google Play Services availability
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(requireContext())
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 9000)?.show()
            } else {
                Snackbar.make(view, "Google Play Services is required", Snackbar.LENGTH_LONG).show()
            }
            return
        }

        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = NotificationsAdapter(
            onNotificationClick = { notification ->
                handleNotificationClick(notification)
            },
            onDeleteClick = { notification ->
                viewModel.deleteNotification(notification.id)
            }
        )
        binding.recyclerViewNotifications.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadNotifications()
        }
    }

    private fun handleNotificationClick(notification: Notification) {
        // Mark notification as read
        viewModel.markAsRead(notification.id)

        // Get location data from notification data map
        val latitude = notification.data?.get("latitude")?.toFloatOrNull() ?: 0f
        val longitude = notification.data?.get("longitude")?.toFloatOrNull() ?: 0f
        val title = notification.data?.get("title") ?: ""

        // Navigate based on notification type
        when (notification.type) {
            Notification.TYPE_NEW_FOOD_NEARBY, Notification.TYPE_POST_CLAIMED -> {
                notification.relatedPostId?.let { postId ->
                    findNavController().navigate(
                        NotificationsFragmentDirections.actionNavigationNotificationsToPostDetails(
                            postId = postId,
                            title = title,
                            latitude = latitude,
                            longitude = longitude
                        )
                    )
                }
            }
            Notification.TYPE_POST_EXPIRED -> {
                findNavController().navigate(
                    NotificationsFragmentDirections.actionNavigationNotificationsToProfile()
                )
            }
            else -> {
                // For system notifications or unknown types, just mark as read
                Snackbar.make(binding.root, notification.message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notifications.collectLatest { notifications ->
                adapter.submitList(notifications)
                binding.emptyView.visibility = if (notifications.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.swipeRefreshLayout.isRefreshing = isLoading
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { error ->
                error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
