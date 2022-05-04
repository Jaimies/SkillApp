package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import android.view.ViewGroup
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.dp

class SkillGroupFooterDelegateAdapter : DelegateAdapter<SkillGroupFooter, SkillGroupFooterViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): SkillGroupFooterViewHolder {
        val view = View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(parent.width, 10.dp.toPx(parent.context))
        }
        return SkillGroupFooterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillGroupFooterViewHolder, item: SkillGroupFooter) {
        holder.group = item.group
    }
}

class SkillGroupFooterViewHolder(view: View) : SkillListViewHolder(view) {
    var group: SkillGroup? = null
    override val unit: MeasurementUnit get() = group!!.unit
    override val groupId get() = -1
}
