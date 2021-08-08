package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.adapter.ListAdapter
import javax.inject.Inject

class SkillOnlyListAdapter @Inject constructor(
    skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
) : ListAdapter<Any, SkillViewHolder>(SkillDiffCallback()) {
    private val skillDelegateAdapter = skillDelegateAdapterFactory.create({}, {})

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return skillDelegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        skillDelegateAdapter.onBindViewHolder(holder, getItem(position) as Skill)
    }
}
