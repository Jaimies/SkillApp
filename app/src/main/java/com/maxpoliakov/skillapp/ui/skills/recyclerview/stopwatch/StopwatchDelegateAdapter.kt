package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.shared.extensions.inflateDataBinding
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StopwatchDelegateAdapter @AssistedInject constructor(
    private val viewHolderFactory: StopwatchViewHolder.Factory,
    @Assisted
    private val callback: SkillsFragmentCallback,
) : DelegateAdapter<StopwatchUiModel, StopwatchViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): StopwatchViewHolder {
        val binding = parent.inflateDataBinding<StopwatchBannerBinding>(R.layout.stopwatch_banner).apply {
            this.lifecycleOwner = lifecycleOwner
        }

        return viewHolderFactory.create(binding, callback)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, item: StopwatchUiModel) {}

    @AssistedFactory
    interface Factory {
        fun create(callback: SkillsFragmentCallback): StopwatchDelegateAdapter
    }
}
