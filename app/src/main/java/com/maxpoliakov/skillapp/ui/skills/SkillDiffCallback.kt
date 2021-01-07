package com.maxpoliakov.skillapp.ui.skills

import androidx.recyclerview.widget.DiffUtil
import com.maxpoliakov.skillapp.domain.model.Skill

class SkillDiffCallback : DiffUtil.ItemCallback<Skill>() {
    override fun areItemsTheSame(oldItem: Skill, newItem: Skill): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Skill, newItem: Skill): Boolean {
        return oldItem == newItem
    }
}
