package com.maxpoliakov.skillapp.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.BarChart
import com.maxpoliakov.skillapp.databinding.HistoryFragBinding
import com.maxpoliakov.skillapp.ui.common.history.FragmentWithHistory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : FragmentWithHistory(-1) {
    private lateinit var binding: HistoryFragBinding

    override val chart: BarChart? = null
    override val viewModel: HistoryViewModel by viewModels()
    override val recyclerView get() = binding.recyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = HistoryFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onMenuItemSelected(id: Int) = false

    override fun onHistoryEmpty() {
        binding.emptyListLayout.root.isVisible = true
    }

    override fun onHistoryNotEmpty() {
        binding.emptyListLayout.root.isVisible = false
    }
}
