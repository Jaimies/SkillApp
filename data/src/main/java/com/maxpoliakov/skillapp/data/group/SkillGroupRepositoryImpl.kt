package com.maxpoliakov.skillapp.data.group

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillGroupRepositoryImpl @Inject constructor(
    private val groupDao: GroupDao,
) : SkillGroupRepository {

    override fun getSkillGroups(): Flow<List<SkillGroup>> {
        return groupDao.getGroups().mapList { it.mapToDomain() }
    }

    override suspend fun addSkillToGroup(skillId: Int, groupId: Int) {
        groupDao.addSkillToGroup(skillId, groupId)
    }

    override suspend fun createGroup(name: String, skills: List<Skill>) {
        groupDao.createGroup(name, skills.map { skill -> skill.id })
    }
}
