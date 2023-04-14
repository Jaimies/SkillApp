package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.shared.recyclerview.BaseViewHolder
import com.maxpoliakov.skillapp.shared.tracking.RecordUtil
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StopwatchViewHolder @AssistedInject constructor(
    @Assisted
    private val binding: StopwatchBannerBinding,
    @Assisted
    private val callback: SkillsFragmentCallback,
    viewModel: StopwatchViewModel,
    private val recordUtil: RecordUtil,
) : BaseViewHolder(binding) {
    init {
        binding.viewModel = viewModel

        viewModel.navigateToSkill.observe(lifecycleOwner) { skill ->
            callback.navigateToSkillDetail(binding.root, skill)
        }

        viewModel.showRecordsAdded.observe(lifecycleOwner, recordUtil::notifyRecordsAdded)
    }

    @AssistedFactory
    interface Factory {
        fun create(binding: StopwatchBannerBinding, callback: SkillsFragmentCallback): StopwatchViewHolder
    }
}
