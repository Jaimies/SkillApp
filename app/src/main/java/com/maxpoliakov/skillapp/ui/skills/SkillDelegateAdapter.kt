package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject
import javax.inject.Provider

class SkillDelegateAdapter(
    private val viewModelProvider: Provider<SkillViewModel>,
    private val navigateToDetails: (skill: Skill) -> Unit
) : DelegateAdapter<Skill, SkillViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): SkillViewHolder {
        parent.inflateDataBinding<SkillsItemBinding>(R.layout.skills_item).run {
            val viewModel = viewModelProvider.get()
            this.viewModel = viewModel
            return SkillViewHolder(root, viewModel, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: SkillViewHolder, item: Skill) {
        holder.setItem(item)
    }

    class Factory @Inject constructor(
        private val viewModelProvider: Provider<SkillViewModel>,
    ) {
        fun create(navigateToDetails: (skill: Skill) -> Unit) : SkillDelegateAdapter {
            return SkillDelegateAdapter(viewModelProvider, navigateToDetails)
        }
    }
}
