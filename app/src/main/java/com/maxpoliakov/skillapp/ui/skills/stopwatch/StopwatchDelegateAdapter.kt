package com.maxpoliakov.skillapp.ui.skills.stopwatch

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject

class StopwatchDelegateAdapter @Inject constructor(
    private val stopwatchViewModel: StopwatchViewModel,
    private val recordUtil: RecordUtil,
) : DelegateAdapter<Unit, StopwatchViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): StopwatchViewHolder {
        val binding = parent.inflateDataBinding<StopwatchBannerBinding>(R.layout.stopwatch_banner).apply {
            viewModel = stopwatchViewModel
        }

        return StopwatchViewHolder(binding.root, parent, recordUtil, stopwatchViewModel)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, item: Unit) {}
}
