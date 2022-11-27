package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.coroutines.flow.Flow

interface GetSkillsAndSkillGroupsUseCase {
    fun getSkills(): Flow<List<Skill>>
    fun getSkillsWithLastWeekTime(unit: MeasurementUnit): Flow<List<Skill>>
    fun getTopSkills(count: Int): Flow<List<Skill>>
    fun getSkillsAndGroups(): Flow<SkillsAndGroups>
}
