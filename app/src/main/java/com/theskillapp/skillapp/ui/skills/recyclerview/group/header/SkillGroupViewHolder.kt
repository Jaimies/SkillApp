package com.theskillapp.skillapp.ui.skills.recyclerview.group.header

import com.theskillapp.skillapp.databinding.SkillGroupHeaderBinding
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.shared.recyclerview.BaseViewHolder
import com.theskillapp.skillapp.shared.recyclerview.itemdecoration.fakecardview.PartOfFakeCardView
import com.theskillapp.skillapp.ui.skills.SkillsFragmentCallback

class SkillGroupViewHolder(
    binding: SkillGroupHeaderBinding,
    callback: SkillsFragmentCallback,
    private val viewModel: SkillGroupViewModel,
) : BaseViewHolder(binding), PartOfFakeCardView {

    override val cardId get() = viewModel.skillGroup.value!!.id

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
