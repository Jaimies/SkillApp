package com.maxpoliakov.skillapp.ui.skills

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.util.navigation.NavigationUtil
import javax.inject.Inject

class SkillViewHolder(
    binding: SkillsItemBinding,
    navigationUtil: NavigationUtil,
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

        viewModel.navigateToDetails.observe {
            viewModel.skill.value?.let { skill ->
                navigationUtil.navigateToSkillDetail(skill.id)
            }
        }

        viewModel.startDrag.observe {
            startDrag(this)
        }
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)

    class Factory @Inject constructor(
        private val navigationUtil: NavigationUtil,
    ) {
        fun create(
            binding: SkillsItemBinding,
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ) = SkillViewHolder(binding, navigationUtil, startDrag)
    }
}
