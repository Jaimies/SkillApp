package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSkillsAndSkillGroupsUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val skillGroupRepository: SkillGroupRepository,
) : GetSkillsAndSkillGroupsUseCase {
    override fun getSkills(): Flow<List<Skill>> {
        return skillRepository.getSkills()
    }

    override fun getSkillsWithLastWeekTime(unit: MeasurementUnit): Flow<List<Skill>> {
        return skillRepository.getSkillsWithLastWeekCount(unit)
    }

    override fun getTopSkills(count: Int): Flow<List<Skill>> {
        return skillRepository.getTopSkills(count)
    }

    override fun getSkillsAndGroups(): Flow<SkillsAndGroups> {
        val groups = skillGroupRepository.getSkillGroups()
        val skills = skillRepository.getSkills().map {
            it.filter { skill -> skill.groupId == -1 }
        }

        return skills.combine(groups) { skills, groups ->
            SkillsAndGroups(skills, groups)
        }
    }
}

data class SkillsAndGroups(
    val skills: List<Skill>,
    val groups: List<SkillGroup>
)
