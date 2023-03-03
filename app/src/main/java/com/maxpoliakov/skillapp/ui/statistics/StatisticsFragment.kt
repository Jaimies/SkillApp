package com.maxpoliakov.skillapp.ui.statistics

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StatisticsFragBinding
import com.maxpoliakov.skillapp.ui.common.DataBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : DataBindingFragment<StatisticsFragBinding>() {
    override val layoutId get() = R.layout.statistics_frag

    private val viewModel: StatisticsViewModel by viewModels()

    override fun onBindingCreated(binding: StatisticsFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }
}
