package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.increaseTouchAreaBy
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

class SkillDelegateAdapter @AssistedInject constructor(
    private val viewModelProvider: Provider<SkillViewModel>,
    private val recordUtil: RecordUtil,
    @Assisted
    private val callback: SkillsFragmentCallback,
) : DelegateAdapter<Skill, SkillViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): SkillViewHolder {
        parent.inflateDataBinding<SkillsItemBinding>(R.layout.skills_item).run {
            val viewModel = viewModelProvider.get()
            this.viewModel = viewModel
            this.startTimer.increaseTouchAreaBy(15.dp)
            return SkillViewHolder(this, recordUtil, callback)
        }
    }

    override fun onBindViewHolder(holder: SkillViewHolder, item: Skill) {
        holder.setItem(item)
    }

    @AssistedFactory
    interface Factory {
        fun create(callback: SkillsFragmentCallback): SkillDelegateAdapter
    }
}
