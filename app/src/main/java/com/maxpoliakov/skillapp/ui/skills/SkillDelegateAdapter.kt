package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

class SkillDelegateAdapter(
    private val navigateToDetails: (skill: Skill) -> Unit
) : DelegateAdapter<Skill, SkillViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): SkillViewHolder {
        parent.inflateDataBinding<SkillsItemBinding>(R.layout.skills_item).run {
            val viewModel = SkillViewModel().also { viewModel = it }
            return SkillViewHolder(root, viewModel, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: SkillViewHolder, item: Skill) {
        holder.setItem(item)
    }
}
