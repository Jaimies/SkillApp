package com.maxpoliakov.skillapp.domain.usecase.skill

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase.SkillsAndGroups
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSkillsAndSkillGroupsUseCaseImpl @Inject constructor(
    private val skillRepository: SkillRepository,
    private val skillGroupRepository: SkillGroupRepository,
) : GetSkillsAndSkillGroupsUseCase {
    override fun getSkills(): Flow<List<Skill>> {
        return skillRepository.getSkills()
    }

    override fun getSkills(criteria: SkillSelectionCriteria): Flow<List<Skill>> {
        return skillRepository.getSkills(criteria)
    }

    override fun getSkillsAndGroups(): Flow<SkillsAndGroups> {
        val groups = skillGroupRepository.getSkillGroups()
        val skills = skillRepository.getSkills(SkillSelectionCriteria.NotInAGroup)

        return skills.combine(groups) { skills, groups ->
            SkillsAndGroups(skills, groups)
        }
    }
}
