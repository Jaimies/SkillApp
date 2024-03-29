package com.maxpoliakov.skillapp.ui.skills.recyclerview.skill

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback
import com.maxpoliakov.skillapp.shared.tracking.RecordUtil
import com.maxpoliakov.skillapp.shared.extensions.increaseTouchAreaBy
import com.maxpoliakov.skillapp.shared.extensions.inflateDataBinding
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

    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): SkillViewHolder {
        parent.inflateDataBinding<SkillsItemBinding>(R.layout.skills_item).run {
            this.lifecycleOwner = lifecycleOwner
            this.viewModel = viewModelProvider.get()
            this.startTimer.increaseTouchAreaBy(30.dp)
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
