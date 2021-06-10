package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class SkillViewHolder(
    view: View,
    private val viewModel: SkillViewModel,
    navigateToDetail: (skill: Skill) -> Unit,
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : BaseViewHolder(view) {

    init {
        viewModel.navigateToDetails.observe {
            viewModel.skill.value?.let(navigateToDetail)
        }

        viewModel.startDrag.observe {
            startDrag(this)
        }
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)
}
