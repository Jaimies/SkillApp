package com.maxpoliakov.skillapp.ui.skills

import android.view.MotionEvent
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.util.tracking.RecordUtil

class SkillViewHolder(
    private val binding: SkillsItemBinding,
    private val recordUtil: RecordUtil,
    callback: SkillsFragmentCallback,
) : SkillListViewHolder(binding.root) {
    val viewModel = binding.viewModel!!

    init {
        binding.dragHandle.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                callback.startDrag(this)
            }

            false
        }

        viewModel.startDrag.observe {
            callback.startDrag(this)
        }

        viewModel.navigateToDetails.observe { skill ->
            callback.navigateToSkillDetail(binding.card, skill)
        }

        viewModel.notifyRecordAdded.observe { record ->
            recordUtil.notifyRecordAdded(binding.root, record)
        }
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)

    var isSmall: Boolean
        get() = viewModel.isSmall.value!!
        set(value) = viewModel.setIsSmall(value)

    var isHighlighted: Boolean
        get() = viewModel.isHighlighted.value!!
        set(value) = viewModel.setIsHighlighted(value)

    val canBeGrouped get() = viewModel.canBeGrouped
    override val groupId get() = viewModel.groupId
    val isInAGroup get() = viewModel.isInAGroup
}
