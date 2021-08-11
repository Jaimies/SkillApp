package com.maxpoliakov.skillapp.ui.skills.group

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject

class SkillGroupDelegateAdapter constructor(
    private val viewHolderFactory: SkillGroupViewHolder.Factory,
    private val startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : DelegateAdapter<SkillGroup, SkillGroupViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): SkillGroupViewHolder {
        parent.inflateDataBinding<SkillGroupBinding>(R.layout.skill_group).run {
            return viewHolderFactory.create(this, startDrag)
        }
    }

    override fun onBindViewHolder(holder: SkillGroupViewHolder, item: SkillGroup) {
        holder.setSkillGroup(item)
    }

    class Factory @Inject constructor(
        private val viewHolderFactory: SkillGroupViewHolder.Factory,
    ) {
        fun create(
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ) = SkillGroupDelegateAdapter(viewHolderFactory, startDrag)
    }
}
