package com.theskillapp.skillapp.domain.usecase.skill

import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import kotlinx.coroutines.flow.Flow

interface GetSkillsAndSkillGroupsUseCase {
    fun getSkills(): Flow<List<Skill>>
    fun getSkills(criteria: SkillSelectionCriteria): Flow<List<Skill>>
    fun getSkillsAndGroups(): Flow<SkillsAndGroups>

    data class SkillsAndGroups(
        val skills: List<Skill>,
        val groups: List<SkillGroup>
    )
}
