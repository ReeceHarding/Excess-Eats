package com.yourcompany.excesseats.ui.claimed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourcompany.excesseats.databinding.FragmentClaimedBinding
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.ui.discovery.adapters.ClaimedFoodPostAdapter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ClaimedFragment : Fragment() {
    private var _binding: FragmentClaimedBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository.getInstance()
    private val foodPostRepository = FoodPostRepository.getInstance()

    private val foodPostAdapter = ClaimedFoodPostAdapter { post ->
        val action = ClaimedFragmentDirections.actionNavigationClaimedToFoodPostDetailFragment(
            postId = post.id,
            title = post.title,
            latitude = post.latitude.toFloat(),
            longitude = post.longitude.toFloat()
        )
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClaimedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadClaimedPosts()
    }

    private fun setupRecyclerView() {
        binding.claimedRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodPostAdapter
        }
    }

    private fun loadClaimedPosts() {
        val currentUser = userRepository.getCurrentUser() ?: return

        lifecycleScope.launch {
            try {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    foodPostRepository.getClaimedPosts(currentUser.uid).collect { result ->
                        result.onSuccess { posts ->
                            if (posts.isEmpty()) {
                                binding.emptyStateText.visibility = View.VISIBLE
                                binding.claimedRecyclerView.visibility = View.GONE
                            } else {
                                binding.emptyStateText.visibility = View.GONE
                                binding.claimedRecyclerView.visibility = View.VISIBLE
                                foodPostAdapter.submitList(posts)
                            }
                        }.onFailure { exception ->
                            if (exception !is CancellationException) {
                                Toast.makeText(
                                    context,
                                    "Error loading claimed posts: ${exception.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                if (e !is CancellationException) {
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 