package com.maxpoliakov.skillapp.ui.statistics

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StatisticsFragBinding
import com.maxpoliakov.skillapp.shared.DataBindingFragment
import com.maxpoliakov.skillapp.shared.time.DateFormatter
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
