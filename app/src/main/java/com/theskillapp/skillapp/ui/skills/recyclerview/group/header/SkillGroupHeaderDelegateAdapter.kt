package com.theskillapp.skillapp.ui.skills.recyclerview.group.header

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.databinding.SkillGroupHeaderBinding
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.theskillapp.skillapp.ui.skills.SkillsFragmentCallback
import com.theskillapp.skillapp.shared.extensions.inflateDataBinding

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
