package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import com.maxpoliakov.skillapp.util.ui.setupAdapter
import javax.inject.Inject

class SkillGroupDelegateAdapter constructor(
    private val viewHolderFactory: SkillGroupViewHolder.Factory,
    private val startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : DelegateAdapter<SkillGroup, SkillGroupViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup): SkillGroupViewHolder {
        parent.inflateDataBinding<SkillGroupBinding>(R.layout.skill_group).run {
            val viewModel = SkillGroupViewModel()
            this.viewModel = viewModel
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

class SkillGroupViewModel {
    private val _skillGroup = MutableLiveData<SkillGroup>()
    val skillGroup: LiveData<SkillGroup> get() = _skillGroup

    fun setSkillGroup(skillGroup: SkillGroup) {
        _skillGroup.value = skillGroup
    }
}
