package com.maxpoliakov.skillapp.data.skill

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkillRepositoryImpl @Inject constructor(
    private val skillDao: SkillDao,
) : SkillRepository {

    private val _skills by lazy {
        skillDao.getSkills().mapList { it.mapToDomain() }
    }

    override fun getSkills() = _skills

    override fun getSkillFlowById(id: Int) = skillDao.getSkillFlow(id)
        .filterNotNull()
        .map { it.mapToDomain() }

    override suspend fun getSkillById(id: Id): Skill? {
        return skillDao.getSkill(id)?.mapToDomain()
    }

    override suspend fun addSkill(skill: Skill) =
        skillDao.insert(skill.mapToDB())

    override suspend fun updateName(skillId: Int, newName: String) {
        skillDao.updateName(skillId, newName)
    }

    override suspend fun deleteSkill(skill: Skill) =
        skillDao.delete(skill.mapToDB())

    override suspend fun increaseTime(id: Id, time: Duration) {
        skillDao.increaseTime(id, time)
    }

    override suspend fun updateOrder(skillId: Int, newOrder: Int) {
        skillDao.setOrder(skillId, newOrder)
    }

    override suspend fun decreaseTime(id: Id, time: Duration) {
        skillDao.increaseTime(id, time.negated())
    }
}
