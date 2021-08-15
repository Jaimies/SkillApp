package com.maxpoliakov.skillapp.ui.skills.group

import com.maxpoliakov.skillapp.databinding.SkillGroupHeaderBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import javax.inject.Inject
import javax.inject.Provider

class SkillGroupViewHolder(
    binding: SkillGroupHeaderBinding,
    val viewModel: SkillGroupViewModel,
) : BaseViewHolder(binding.root) {

    init {
        binding.viewModel = viewModel
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        viewModel.setSkillGroup(skillGroup)
    }

    class Factory @Inject constructor(
        private val viewModelProvider: Provider<SkillGroupViewModel>,
    ) {
        fun create(binding: SkillGroupHeaderBinding): SkillGroupViewHolder {
            return SkillGroupViewHolder(binding, viewModelProvider.get())
        }
    }
}
