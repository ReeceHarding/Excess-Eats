package com.yourcompany.excesseats.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourcompany.excesseats.databinding.FragmentClaimedMealsBinding
import com.yourcompany.excesseats.data.repository.FoodPostRepository
import com.yourcompany.excesseats.data.repository.UserRepository
import com.yourcompany.excesseats.ui.discovery.adapters.FoodPostAdapter
import com.yourcompany.excesseats.utils.Logger
import kotlinx.coroutines.launch

class ClaimedMealsFragment : Fragment() {
    private var _binding: FragmentClaimedMealsBinding? = null
    private val binding get() = _binding!!
    private val userRepository = UserRepository.getInstance()
    private val foodPostRepository = FoodPostRepository.getInstance()
    
    private val foodPostAdapter = FoodPostAdapter { post ->
        // Navigate to food post detail when clicked
        val action = ClaimedMealsFragmentDirections.actionClaimedMealsFragmentToFoodPostDetailFragment(
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
        _binding = FragmentClaimedMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        loadClaimedMeals()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        binding.claimedMealsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodPostAdapter
        }
    }

    private fun loadClaimedMeals() {
        val currentUser = userRepository.getCurrentUser() ?: return
        
        lifecycleScope.launch {
            try {
                foodPostRepository.getClaimedPosts(currentUser.uid).collect { result ->
                    result.onSuccess { posts ->
                        if (posts.isEmpty()) {
                            binding.emptyStateText.visibility = View.VISIBLE
                            binding.claimedMealsRecyclerView.visibility = View.GONE
                        } else {
                            binding.emptyStateText.visibility = View.GONE
                            binding.claimedMealsRecyclerView.visibility = View.VISIBLE
                            foodPostAdapter.submitList(posts)
                        }
                    }.onFailure { exception ->
                        Logger.e("Error loading claimed meals", exception)
                        Toast.makeText(
                            context,
                            "Error loading claimed meals: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Logger.e("Error loading claimed meals", e)
                Toast.makeText(
                    context,
                    "Error loading claimed meals: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 