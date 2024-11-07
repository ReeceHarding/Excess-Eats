package com.exceseats.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.exceseats.databinding.FragmentProfileStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileStatsFragment : Fragment() {

    private var _binding: FragmentProfileStatsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.statsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StatsState.Success -> {
                    binding.apply {
                        mealsClaimedTextView.text = state.stats.mealsClaimed.toString()
                        wastePreventedTextView.text = "${state.stats.wasteReduced} kg"
                        foodPostsCard.isVisible = viewModel.isCaterer()
                        foodPostsTextView.text = state.stats.foodPosts.toString()
                    }
                }
                is StatsState.Error -> {
                    // Handle error state
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
