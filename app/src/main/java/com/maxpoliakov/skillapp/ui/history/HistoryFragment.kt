package com.maxpoliakov.skillapp.ui.history

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.BarChart
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.HistoryFragBinding
import com.maxpoliakov.skillapp.ui.common.history.FragmentWithHistory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : FragmentWithHistory<HistoryFragBinding>(-1) {
    override val layoutId get() = R.layout.history_frag

    override val chart: BarChart? = null
    override val viewModel: HistoryViewModel by viewModels()
    override val recyclerView get() = binding.recyclerView

    override fun onBindingCreated(binding: HistoryFragBinding) {
        binding.viewModel = viewModel
    }

    override fun onMenuItemSelected(id: Int) = false

    override fun onHistoryEmpty() {
        binding.emptyListLayout.root.isVisible = true
    }

    override fun onHistoryNotEmpty() {
        binding.emptyListLayout.root.isVisible = false
    }
}
