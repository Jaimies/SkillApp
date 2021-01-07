package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

class SkillViewHolder(
    view: View,
    private val viewModel: SkillViewModel,
    navigateToDetail: (index: Int) -> Unit
) : BaseViewHolder(view) {

    init {
        viewModel.navigateToDetails.observe { navigateToDetail(adapterPosition) }
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)
}
