package com.maxpoliakov.skillapp.ui.skills.group

import com.maxpoliakov.skillapp.databinding.SkillGroupHeaderBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.skills.SkillListViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback

class SkillGroupViewHolder(
    binding: SkillGroupHeaderBinding,
    callback: SkillsFragmentCallback,
    val viewModel: SkillGroupViewModel,
) : SkillListViewHolder(binding.root) {

    override val groupId get() = viewModel.skillGroup.value!!.id

    init {
        binding.viewModel = viewModel

        viewModel.navigateToDetail.observe { group ->
            callback.navigateToGroupDetail(binding.root, group)
        }
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        viewModel.setSkillGroup(skillGroup)
    }
}
