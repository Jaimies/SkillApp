package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class SkillViewHolder(
    view: View,
    private val viewModel: SkillViewModel,
    navigateToDetail: (skill: Skill) -> Unit
) : BaseViewHolder(view) {

    init {
        viewModel.navigateToDetails.observe {
            viewModel.skill.value?.let(navigateToDetail)
        }
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)
}
