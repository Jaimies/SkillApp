package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.increaseTouchAreaBy
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding
import javax.inject.Inject
import javax.inject.Provider

class SkillDelegateAdapter(
    private val viewModelProvider: Provider<SkillViewModel>,
    private val startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
) : DelegateAdapter<Skill, SkillViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): SkillViewHolder {
        parent.inflateDataBinding<SkillsItemBinding>(R.layout.skills_item).run {
            val viewModel = viewModelProvider.get()
            this.viewModel = viewModel
            this.startTimer.increaseTouchAreaBy(35.dp)
            return SkillViewHolder(this, startDrag)
        }
    }

    override fun onBindViewHolder(holder: SkillViewHolder, item: Skill) {
        holder.setItem(item)
    }

    class Factory @Inject constructor(
        private val viewModelProvider: Provider<SkillViewModel>,
    ) {
        fun create(
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ): SkillDelegateAdapter {
            return SkillDelegateAdapter(viewModelProvider, startDrag)
        }
    }
}
