package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupBinding
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import javax.inject.Inject

class SkillGroupDelegateAdapter @Inject constructor(
    private val viewHolderFactory: SkillGroupViewHolder.Factory,
) : DelegateAdapter<SkillGroup, SkillGroupViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): SkillGroupViewHolder {
        parent.inflateDataBinding<SkillGroupBinding>(R.layout.skill_group).run {
            val viewModel = SkillGroupViewModel()
            this.viewModel = viewModel
            return viewHolderFactory.create(this)
        }
    }

    override fun onBindViewHolder(holder: SkillGroupViewHolder, item: SkillGroup) {
        holder.setSkillGroup(item)
    }
}

class SkillGroupViewHolder(
    private val listAdapter: SkillOnlyListAdapter,
    private val binding: SkillGroupBinding,
) : BaseViewHolder(binding.root) {
    init {
        binding.recyclerView.setupAdapter(listAdapter)
    }

    fun setSkillGroup(skillGroup: SkillGroup) {
        listAdapter.submitList(skillGroup.skills)
        binding.viewModel!!.setSkillGroup(skillGroup)
    }

    class Factory @Inject constructor(
        private val adapter: SkillOnlyListAdapter,
    ) {
        fun create(binding: SkillGroupBinding): SkillGroupViewHolder {
            return SkillGroupViewHolder(adapter, binding)
        }
    }
}

class SkillGroupViewModel {
    private val _skillGroup = MutableLiveData<SkillGroup>()
    val skillGroup: LiveData<SkillGroup> get() = _skillGroup

    fun setSkillGroup(skillGroup: SkillGroup) {
        _skillGroup.value = skillGroup
    }
}
