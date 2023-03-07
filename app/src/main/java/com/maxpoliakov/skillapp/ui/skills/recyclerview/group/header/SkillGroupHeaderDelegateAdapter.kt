package com.maxpoliakov.skillapp.ui.skills.recyclerview.group.header

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupHeaderBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

class SkillGroupHeaderDelegateAdapter(
    private val callback: SkillsFragmentCallback,
) : DelegateAdapter<SkillGroup, SkillGroupViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): SkillGroupViewHolder {
        parent.inflateDataBinding<SkillGroupHeaderBinding>(R.layout.skill_group_header).run {
            this.lifecycleOwner = lifecycleOwner
            return SkillGroupViewHolder(this, callback, SkillGroupViewModel())
        }
    }

    override fun onBindViewHolder(holder: SkillGroupViewHolder, item: SkillGroup) {
        holder.setSkillGroup(item)
    }
}
