package com.maxpoliakov.skillapp.ui.skills

import android.view.MotionEvent
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class SkillViewHolder(
    private val binding: SkillsItemBinding,
    callback: SkillsFragmentCallback,
) : BaseViewHolder(binding.root) {
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
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)

    var isSmall: Boolean
        get() = viewModel.isSmall.value!!
        set(value) = viewModel.setIsSmall(value)

    var isHighlighted: Boolean
        get() = viewModel.isHighlighted.value!!
        set(value) = viewModel.setIsHighlighted(value)
}
