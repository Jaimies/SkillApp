package com.theskillapp.skillapp.domain.usecase.skill

import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.Skill

interface ManageSkillUseCase {
    suspend fun addSkill(skill: Skill): Long
    suspend fun updateSkill(skillId: Int, newName: String, newGoal: Goal?)
    suspend fun deleteSkill(skill: Skill)
}
