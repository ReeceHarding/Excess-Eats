package com.yourcompany.excesseats.ui.posting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yourcompany.excesseats.databinding.FragmentItemPostingBinding

class ItemPostingFragment : Fragment() {
    private var _binding: FragmentItemPostingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: Set up form for posting new items
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
