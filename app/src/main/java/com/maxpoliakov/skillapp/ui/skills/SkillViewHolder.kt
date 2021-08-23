package com.maxpoliakov.skillapp.ui.skills

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class SkillViewHolder(
    binding: SkillsItemBinding,
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : BaseViewHolder(binding.root) {
    val viewModel = binding.viewModel!!

    init {
        binding.dragHandle.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                startDrag(this)
            }

            false
        }

        viewModel.startDrag.observe {
            startDrag(this)
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
