package com.maxpoliakov.skillapp.ui.skills.group

import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.databinding.SkillGroupBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillOnlyListAdapter
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import javax.inject.Inject
import javax.inject.Provider

class SkillGroupViewHolder(
    adapterFactory: SkillOnlyListAdapter.Factory,
    binding: SkillGroupBinding,
    val viewModel: SkillGroupViewModel,
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : BaseViewHolder(binding.root) {
    private val adapter = adapterFactory.create(startDrag)

    init {
        binding.viewModel = viewModel
        binding.recyclerView.setupAdapter(adapter)
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        adapter.submitList(skillGroup.skills)
        viewModel.setSkillGroup(skillGroup)
    }

    class Factory @Inject constructor(
        private val adapterFactory: SkillOnlyListAdapter.Factory,
        private val viewModelProvider: Provider<SkillGroupViewModel>,
    ) {
        fun create(
            binding: SkillGroupBinding,
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ): SkillGroupViewHolder {
            return SkillGroupViewHolder(adapterFactory, binding, viewModelProvider.get(), startDrag)
        }
    }
}
