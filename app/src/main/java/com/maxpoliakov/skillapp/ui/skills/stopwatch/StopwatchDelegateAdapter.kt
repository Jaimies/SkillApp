package com.maxpoliakov.skillapp.ui.skills.stopwatch

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject

class StopwatchDelegateAdapter @Inject constructor(
    private val viewHolderFactory: StopwatchViewHolder.Factory,
) : DelegateAdapter<StopwatchUiModel, StopwatchViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): StopwatchViewHolder {
        val binding = parent.inflateDataBinding<StopwatchBannerBinding>(R.layout.stopwatch_banner).apply {
            this.lifecycleOwner = lifecycleOwner
        }

        return viewHolderFactory.create(binding)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, item: StopwatchUiModel) {}
}
