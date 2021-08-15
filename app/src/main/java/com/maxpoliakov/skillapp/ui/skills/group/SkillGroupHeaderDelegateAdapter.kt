package com.maxpoliakov.skillapp.ui.skills.group

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupHeaderBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject

class SkillGroupHeaderDelegateAdapter @Inject constructor(
    private val viewHolderFactory: SkillGroupViewHolder.Factory,
) : DelegateAdapter<SkillGroup, SkillGroupViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): SkillGroupViewHolder {
        parent.inflateDataBinding<SkillGroupHeaderBinding>(R.layout.skill_group_header).run {
            return viewHolderFactory.create(this)
        }
    }

    override fun onBindViewHolder(holder: SkillGroupViewHolder, item: SkillGroup) {
        holder.setSkillGroup(item)
    }
}
