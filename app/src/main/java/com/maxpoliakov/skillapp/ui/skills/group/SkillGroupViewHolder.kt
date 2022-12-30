package com.maxpoliakov.skillapp.ui.skills.group

import com.maxpoliakov.skillapp.databinding.SkillGroupHeaderBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillListViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback

class SkillGroupViewHolder(
    binding: SkillGroupHeaderBinding,
    callback: SkillsFragmentCallback,
    val viewModel: SkillGroupViewModel,
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
