package com.theskillapp.skillapp.ui.skills.recyclerview.stopwatch

import com.theskillapp.skillapp.databinding.StopwatchBannerBinding
import com.theskillapp.skillapp.shared.recyclerview.BaseViewHolder

class StopwatchViewHolder(
    private val binding: StopwatchBannerBinding,
) : BaseViewHolder(binding) {
    fun bind(stopwatch: StopwatchUiModel) {
        binding.setStopwatch(stopwatch)
    }
}
