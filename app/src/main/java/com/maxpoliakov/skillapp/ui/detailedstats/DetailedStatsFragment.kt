package com.maxpoliakov.skillapp.ui.detailedstats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.databinding.DetailedStatsFragBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailedStatsFragment : Fragment() {
    private val viewModel: DetailedStatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DetailedStatsFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }
        return binding.root
    }
}
