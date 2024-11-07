package com.exceseats.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceseats.databinding.FragmentNotificationListBinding
import com.exceseats.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationListFragment : Fragment() {

    private var _binding: FragmentNotificationListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationViewModel by activityViewModels()
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter { notification ->
            viewModel.markAsRead(notification.id)
            // Handle notification click based on type
            when (notification.type) {
                NotificationType.NEARBY -> {
                    // Navigate to food detail
                }
                NotificationType.UPDATE -> {
                    // Navigate to relevant screen
                }
                NotificationType.SYSTEM -> {
                    // Handle system notification
                }
            }
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@NotificationListFragment.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.notificationState.observe(viewLifecycleOwner) { state ->
            binding.progressBar.isVisible = state is NotificationState.Loading
            binding.emptyView.isVisible = state is NotificationState.Success &&
                state.notifications.isEmpty()

            when (state) {
                is NotificationState.Success -> {
                    adapter.submitList(state.notifications)
                }
                is NotificationState.Error -> {
                    requireContext().showToast(state.message)
                }
                else -> Unit
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
