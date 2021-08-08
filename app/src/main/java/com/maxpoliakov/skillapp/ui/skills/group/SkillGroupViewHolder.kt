package com.maxpoliakov.skillapp.ui.skills.group

import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.databinding.SkillGroupBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillOnlyListAdapter
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import javax.inject.Inject

class SkillGroupViewHolder(
    adapterFactory: SkillOnlyListAdapter.Factory,
    private val binding: SkillGroupBinding,
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : BaseViewHolder(binding.root) {
    private val adapter = adapterFactory.create(startDrag)

    init {
        binding.recyclerView.setupAdapter(adapter)
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        adapter.submitList(skillGroup.skills)
        binding.viewModel!!.setSkillGroup(skillGroup)
    }

    class Factory @Inject constructor(
        private val adapterFactory: SkillOnlyListAdapter.Factory,
    ) {
        fun create(
            binding: SkillGroupBinding,
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ): SkillGroupViewHolder {
            return SkillGroupViewHolder(adapterFactory, binding, startDrag)
        }
    }
}
