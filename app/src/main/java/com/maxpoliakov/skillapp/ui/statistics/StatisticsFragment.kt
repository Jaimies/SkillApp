package com.maxpoliakov.skillapp.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StatisticsFragBinding
import com.maxpoliakov.skillapp.ui.common.BarChartFragment
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BarChartFragment(R.menu.stats_frag_menu) {
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

    override fun onMenuItemSelected(id: Int): Boolean {
        if (id == R.id.detailed_stats) {
            findNavController().navigateAnimated(R.id.detailed_stats_fragment_dest)
            return true
        }

        return false
    }
}
