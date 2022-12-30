package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter

class SkillGroupFooterDelegateAdapter : DelegateAdapter<SkillGroupFooter, SkillGroupFooterViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): SkillGroupFooterViewHolder {
        val view = View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(parent.width, 10)
        }
        return SkillGroupFooterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillGroupFooterViewHolder, item: SkillGroupFooter) {
        holder.group = item.group
    }
}

class SkillGroupFooterViewHolder(view: View) : ViewHolder(view), SkillListViewHolder {
    var group: SkillGroup? = null
    override val unit: MeasurementUnit get() = group!!.unit
    override val groupId get() = group?.id ?: -1
}
