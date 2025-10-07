package com.theskillapp.skillapp.ui.statistics

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.databinding.StatisticsFragBinding
import com.theskillapp.skillapp.shared.DataBindingFragment
import com.theskillapp.skillapp.shared.time.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticsFragment : DataBindingFragment<StatisticsFragBinding>() {
    override val layoutId get() = R.layout.statistics_frag

    private val viewModel: StatisticsViewModel by viewModels()

    @Inject
    lateinit var dateFormatter: DateFormatter

    override fun onBindingCreated(binding: StatisticsFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
        binding.dateFormatter = dateFormatter
    }
}
