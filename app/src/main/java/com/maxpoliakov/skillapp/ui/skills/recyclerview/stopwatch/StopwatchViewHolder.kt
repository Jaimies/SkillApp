package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.shared.recyclerview.BaseViewHolder

class StopwatchViewHolder(
    private val binding: StopwatchBannerBinding,
) : BaseViewHolder(binding) {
    fun bind(stopwatch: StopwatchUiModel) {
        binding.setStopwatch(stopwatch)
    }
}
