package com.theskillapp.skillapp.domain.usecase.skill

import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillSelectionCriteria
import com.theskillapp.skillapp.domain.repository.SkillGroupRepository
import com.theskillapp.skillapp.domain.repository.SkillRepository
import com.theskillapp.skillapp.domain.usecase.skill.GetSkillsAndSkillGroupsUseCase.SkillsAndGroups
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
