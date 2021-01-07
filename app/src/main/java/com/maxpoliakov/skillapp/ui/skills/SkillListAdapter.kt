package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.maxpoliakov.skillapp.domain.model.Skill

class SkillListAdapter(
    private val delegateAdapter: SkillDelegateAdapter
) : ListAdapter<Skill, SkillViewHolder>(SkillDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        delegateAdapter.onBindViewHolder(holder, getItem(position))
    }

    public override fun getItem(position: Int): Skill {
        return super.getItem(position)
    }
}
