package com.maxpoliakov.skillapp.ui.skills

import androidx.recyclerview.widget.DiffUtil
import com.maxpoliakov.skillapp.domain.model.Skill

class SkillDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return compare(oldItem, newItem) { old, new -> old.id == new.id }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return compare(oldItem, newItem) { old, new ->
            // order should be ignored when comparing for equality
            old.copy(order = 0) == new.copy(order = 0)
        }
    }

    private fun compare(
        oldItem: Any,
        newItem: Any,
        predicate: (old: Skill, new: Skill) -> Boolean
    ): Boolean {
        return oldItem !is Skill && newItem !is Skill
                || oldItem is Skill && newItem is Skill && predicate(oldItem, newItem)
    }
}
