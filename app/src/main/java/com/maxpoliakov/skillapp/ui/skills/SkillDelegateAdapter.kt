package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.model.SkillItem
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewHolder
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

typealias ViewHolder = RecordableViewHolder<SkillItem>

class SkillDelegateAdapter(
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter<SkillItem, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<SkillsItemBinding>(R.layout.skills_item).run {
            val viewModel = SkillViewModel().also { viewModel = it }
            return RecordableViewHolder(root, viewModel, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: SkillItem) {
        holder.setItem(item)
    }
}
