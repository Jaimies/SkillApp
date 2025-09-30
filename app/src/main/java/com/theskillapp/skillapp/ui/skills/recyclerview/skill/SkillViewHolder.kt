package com.theskillapp.skillapp.ui.skills.recyclerview.skill

import com.theskillapp.skillapp.databinding.SkillsItemBinding
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.shared.recyclerview.BaseViewHolder
import com.theskillapp.skillapp.shared.recyclerview.itemdecoration.fakecardview.PartOfFakeCardView
import com.theskillapp.skillapp.shared.tracking.RecordUtil
import com.theskillapp.skillapp.ui.skills.SkillsFragmentCallback

class SkillViewHolder(
    private val binding: SkillsItemBinding,
    private val recordUtil: RecordUtil,
    callback: SkillsFragmentCallback,
) : BaseViewHolder(binding), PartOfFakeCardView {
    private val viewModel = binding.viewModel!!

    init {
        viewModel.startDrag.observe(lifecycleOwner) {
            callback.startDrag(this)
        }

        viewModel.navigateToDetails.observe(lifecycleOwner) { skill ->
            callback.navigateToSkillDetail(binding.card, skill)
        }

        viewModel.showRecordsAdded.observe(lifecycleOwner, recordUtil::notifyRecordsAdded)
    }

    fun setItem(item: Skill) {
        viewModel.setSkill(item)
        isSmall = item.isInAGroup
    }

    var isSmall: Boolean
        get() = binding.isSmall
        set(value) = binding.setIsSmall(value)

    var isHighlighted: Boolean
        get() = binding.isHighlighted
        set(value) = binding.setIsHighlighted(value)

    override val cardId get() = viewModel.groupId
}
