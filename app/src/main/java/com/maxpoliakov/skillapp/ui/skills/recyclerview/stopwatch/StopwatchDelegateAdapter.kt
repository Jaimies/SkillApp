package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.shared.extensions.inflateDataBinding
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.DelegateAdapter

class StopwatchDelegateAdapter : DelegateAdapter<StopwatchUiModel, StopwatchViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): StopwatchViewHolder {
        val binding = parent.inflateDataBinding<StopwatchBannerBinding>(R.layout.stopwatch_banner).apply {
            this.lifecycleOwner = lifecycleOwner
        }

        return StopwatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, item: StopwatchUiModel) {
        holder.bind(item)
    }
}
