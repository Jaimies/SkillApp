package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

class StopwatchDelegateAdapter(
    private val skillsViewModel: SkillsViewModel,
): DelegateAdapter<Unit, StopwatchViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): StopwatchViewHolder {
        val binding = parent.inflateDataBinding<StopwatchBannerBinding>(R.layout.stopwatch_banner).apply {
            viewModel = skillsViewModel
        }

        return StopwatchViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, item: Unit) {}
}
