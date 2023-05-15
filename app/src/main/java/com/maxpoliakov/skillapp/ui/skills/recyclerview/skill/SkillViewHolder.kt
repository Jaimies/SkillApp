package com.maxpoliakov.skillapp.ui.skills.recyclerview.skill

import android.view.MotionEvent
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.recyclerview.BaseViewHolder
import com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview.PartOfFakeCardView
import com.maxpoliakov.skillapp.shared.tracking.RecordUtil
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback

class SkillViewHolder(
    private val binding: SkillsItemBinding,
    private val recordUtil: RecordUtil,
    callback: SkillsFragmentCallback,
) : BaseViewHolder(binding), PartOfFakeCardView {
    private val viewModel = binding.viewModel!!

    init {
        binding.dragHandleWrapper.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                return@setOnTouchListener viewModel.startDrag()
            }

            false
        }

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
