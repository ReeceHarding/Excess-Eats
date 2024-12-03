package com.yourcompany.excesseats.ui.discovery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yourcompany.excesseats.databinding.FragmentItemDiscoveryBinding

class ItemDiscoveryFragment : Fragment() {
    private var _binding: FragmentItemDiscoveryBinding? = null
    private val binding get() = _binding!!
    private val TAG = "ItemDiscoveryFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            Log.d(TAG, "Creating view")
            _binding = FragmentItemDiscoveryBinding.inflate(inflater, container, false)
            Log.d(TAG, "View binding successful")
            return binding.root
        } catch (e: Exception) {
            Log.e(TAG, "Error creating view", e)
            throw e
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            Log.d(TAG, "View created")

            // Show empty state initially
            binding.recyclerView.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE

            Log.d(TAG, "Initial UI setup complete")
        } catch (e: Exception) {
            Log.e(TAG, "Error in onViewCreated", e)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView called")
        _binding = null
    }
}
