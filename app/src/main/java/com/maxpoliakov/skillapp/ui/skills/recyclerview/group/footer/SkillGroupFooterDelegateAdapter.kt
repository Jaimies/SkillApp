package com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview.PartOfFakeCardView

class SkillGroupFooterDelegateAdapter : DelegateAdapter<SkillGroupFooter, SkillGroupFooterViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): SkillGroupFooterViewHolder {
        return SkillGroupFooterViewHolder(View(parent.context))
    }

    override fun onBindViewHolder(holder: SkillGroupFooterViewHolder, item: SkillGroupFooter) {
        holder.group = item.group
    }
}

class SkillGroupFooterViewHolder(view: View) : ViewHolder(view), PartOfFakeCardView {
    var group: SkillGroup? = null
    override val cardId get() = group?.id ?: -1
}
