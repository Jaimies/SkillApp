package com.maxpoliakov.skillapp.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.databinding.StatisticsFragBinding
import com.maxpoliakov.skillapp.ui.common.BarChartFragment
import com.maxpoliakov.skillapp.util.fragment.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BarChartFragment(-1) {
    private val viewModel: StatisticsViewModel by viewModels()

    private lateinit var binding: StatisticsFragBinding

    override val chart get() = binding.productivityChart.chart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = StatisticsFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onMenuItemSelected(id: Int) = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.pieData.data) { pieData ->
            binding.splitChart.chart.update(pieData)
            binding.splitChart.root.isGone = binding.splitChart.chart.data == null
        }
    }
}
