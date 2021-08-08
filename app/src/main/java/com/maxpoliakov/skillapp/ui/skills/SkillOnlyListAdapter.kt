package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.adapter.ListAdapter
import javax.inject.Inject

class SkillOnlyListAdapter constructor(
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
    skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
) : ListAdapter<Any, SkillViewHolder>(SkillDiffCallback()) {
    private val skillDelegateAdapter = skillDelegateAdapterFactory.create(startDrag)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        return skillDelegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        skillDelegateAdapter.onBindViewHolder(holder, getItem(position) as Skill)
    }

    class Factory @Inject constructor(
        private val skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
    ) {
        fun create(
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ) = SkillOnlyListAdapter(startDrag, skillDelegateAdapterFactory)
    }
}
