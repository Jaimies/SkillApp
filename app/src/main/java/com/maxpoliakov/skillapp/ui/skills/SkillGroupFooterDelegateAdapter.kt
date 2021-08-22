package com.maxpoliakov.skillapp.ui.skills

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter

class SkillGroupFooterDelegateAdapter : DelegateAdapter<SkillGroupFooter, SkillGroupFooterViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): SkillGroupFooterViewHolder {
        val view = View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(parent.width, 1)
        }
        return SkillGroupFooterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillGroupFooterViewHolder, item: SkillGroupFooter) {
        holder.groupId = item.groupId
    }

}

class SkillGroupFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var groupId = -1
}
