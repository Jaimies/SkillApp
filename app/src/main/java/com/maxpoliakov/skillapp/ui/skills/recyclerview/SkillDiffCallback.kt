package com.maxpoliakov.skillapp.ui.skills.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch.StopwatchUiModel

class SkillDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem is StopwatchUiModel && newItem is StopwatchUiModel
                || oldItem is Skill && newItem is Skill && oldItem.id == newItem.id
                || oldItem is SkillGroup && newItem is SkillGroup && newItem.id == oldItem.id
                || oldItem is SkillGroupFooter && newItem is SkillGroupFooter && newItem.group.id == oldItem.group.id
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem is StopwatchUiModel && newItem is StopwatchUiModel && oldItem == newItem
                || oldItem is Skill && newItem is Skill && oldItem.copy(order = 0) == newItem.copy(order = 0)
                || oldItem is SkillGroup && newItem is SkillGroup && newItem == oldItem
                || oldItem is SkillGroupFooter && newItem is SkillGroupFooter
    }
}
