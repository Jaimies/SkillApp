package com.maxpoliakov.skillapp.ui.skills.recyclerview.group.header

import com.maxpoliakov.skillapp.databinding.SkillGroupHeaderBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.shared.recyclerview.BaseViewHolder
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback

class SkillGroupViewHolder(
    binding: SkillGroupHeaderBinding,
    callback: SkillsFragmentCallback,
    private val viewModel: SkillGroupViewModel,
) : BaseViewHolder(binding), SkillListViewHolder {

    override val groupId get() = viewModel.skillGroup.value!!.id
    override val unit get() = viewModel.skillGroup.value!!.unit

    init {
        binding.viewModel = viewModel

        viewModel.navigateToDetail.observe(lifecycleOwner) { group ->
            callback.navigateToGroupDetail(binding.root, group)
        }
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        viewModel.setSkillGroup(skillGroup)
    }
}
