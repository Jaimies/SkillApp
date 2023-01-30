package com.maxpoliakov.skillapp.ui.statistics

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StatisticsFragBinding
import com.maxpoliakov.skillapp.ui.chart.BarChartFragment
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BarChartFragment<StatisticsFragBinding>(R.menu.stats_frag_menu) {
    override val layoutId get() = R.layout.statistics_frag

    private val viewModel: StatisticsViewModel by viewModels()

    override val StatisticsFragBinding.chart get() = productivityChart.chart

    override fun onBindingCreated(binding: StatisticsFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onMenuItemSelected(id: Int): Boolean {
        if (id == R.id.detailed_stats) {
            findNavController().navigateAnimated(R.id.detailed_stats_fragment_dest)
            return true
        }

        return false
    }
}
