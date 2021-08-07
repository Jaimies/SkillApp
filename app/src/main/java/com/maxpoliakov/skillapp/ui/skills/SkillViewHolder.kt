package com.maxpoliakov.skillapp.ui.skills

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class SkillViewHolder(
    binding: SkillsItemBinding,
    val viewModel: SkillViewModel,
    navigateToDetail: (skill: Skill) -> Unit,
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : BaseViewHolder(binding.root) {

    init {

        binding.dragHandle.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                startDrag(this)
            }

            false
        }

        viewModel.navigateToDetails.observe {
            viewModel.skill.value?.let(navigateToDetail)
        }

        viewModel.startDrag.observe {
            startDrag(this)
        }
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)
}
