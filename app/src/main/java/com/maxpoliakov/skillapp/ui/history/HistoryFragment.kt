package com.maxpoliakov.skillapp.ui.history

import android.os.Bundle
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
    override val recyclerView get() = requireBinding().recyclerView

    override fun onBindingCreated(binding: HistoryFragBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
    }

    override fun onHistoryEmpty() {
        requireBinding().emptyListLayout.root.isVisible = true
    }

    override fun onHistoryNotEmpty() {
        requireBinding().emptyListLayout.root.isVisible = false
    }
}
