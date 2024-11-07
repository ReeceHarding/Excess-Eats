package com.exceseats.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.exceseats.databinding.ActivityHomeBinding
import com.exceseats.ui.fooddetail.FoodDetailActivity
import com.exceseats.ui.map.MapActivity
import com.exceseats.ui.postfood.PostFoodActivity
import com.exceseats.util.Constants
import com.exceseats.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: FoodPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViews()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = FoodPostAdapter { foodPost ->
            startActivity(FoodDetailActivity.createIntent(this, foodPost.postId))
        }
        binding.foodPostsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = this@HomeActivity.adapter
        }
    }

    private fun setupViews() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadFoodPosts()
        }

        binding.fabMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        binding.fabPostFood.setOnClickListener {
            startActivity(Intent(this, PostFoodActivity::class.java))
        }

        binding.errorView.retryButton.setOnClickListener {
            viewModel.loadFoodPosts()
        }
    }

    private fun observeViewModel() {
        viewModel.foodPosts.observe(this) { state ->
            binding.swipeRefreshLayout.isRefreshing = false
            binding.shimmerLayout.visibility = if (state is HomeState.Loading) View.VISIBLE else View.GONE

            if (state is HomeState.Loading) {
                binding.shimmerLayout.startShimmer()
            } else {
                binding.shimmerLayout.stopShimmer()
            }

            binding.errorView.visibility = if (state is HomeState.Error) View.VISIBLE else View.GONE
            binding.emptyView.visibility = if (state is HomeState.Success && state.posts.isEmpty())
                View.VISIBLE else View.GONE
            binding.foodPostsRecyclerView.visibility = if (state is HomeState.Success && state.posts.isNotEmpty())
                View.VISIBLE else View.GONE

            when (state) {
                is HomeState.Success -> {
                    adapter.submitList(state.posts)
                }
                is HomeState.Error -> {
                    binding.errorView.errorMessageTextView.text = state.message
                }
                else -> Unit
            }
        }
    }
}
