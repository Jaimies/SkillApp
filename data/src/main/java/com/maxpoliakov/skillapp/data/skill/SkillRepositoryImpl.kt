package com.maxpoliakov.skillapp.data.skill

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.repository.SkillRepository
import com.maxpoliakov.skillapp.shared.util.filterList
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
    override fun getSkillsWithLastWeekCount(unit: MeasurementUnit): Flow<List<Skill>> {
        return _skills
            .filterList { skill -> skill.unit == unit }
            .flatMapLatest { skills ->
                if (skills.isEmpty()) flowOf(listOf())
                else combine(skills.map { skillDao.getSkillFlow(it.id) }) {
                    it.map { it!!.mapToDomain() }
                }
            }
    }

    override fun getSkills(criteria: SelectionCriteria): Flow<List<Skill>> {
        return _skills.filterList { skill -> criteria.isValid(skill) }
    }

    override suspend fun getSkillsWithMeasurementUnit(unit: MeasurementUnit): List<Skill> {
        return skillDao.getSkillsWithMeasurementUnit(unit).map(DBSkill::mapToDomain)
    }

    override fun getSkillFlowById(id: Int) = skillDao.getSkillFlow(id)
        .filterNotNull()
        .map { it.mapToDomain() }

    override fun getTopSkills(count: Int): Flow<List<Skill>> {
        return skillDao.getTopSkills(count).mapList { it.mapToDomain() }
    }

    override suspend fun getSkillById(id: Id): Skill? {
        return skillDao.getSkill(id)?.mapToDomain()
    }

    override suspend fun addSkill(skill: Skill) =
        skillDao.insert(skill.mapToDB())

    override suspend fun updateName(skillId: Int, newName: String) {
        skillDao.updateName(skillId, newName)
    }

    override suspend fun updateGoal(skillId: Int, newGoal: Goal?) {
        if (newGoal == null)
            skillDao.updateGoal(skillId, 0, Goal.Type.Daily)
        else
            skillDao.updateGoal(skillId, newGoal.count, newGoal.type)
    }

    override suspend fun deleteSkill(skill: Skill) =
        skillDao.delete(skill.mapToDB())

    override suspend fun increaseCount(id: Id, count: Long) {
        skillDao.increaseCount(id, count)
    }

    override suspend fun updateOrder(skillId: Int, newOrder: Int) {
        skillDao.setOrder(skillId, newOrder)
    }

    override suspend fun decreaseCount(id: Id, count: Long) {
        skillDao.increaseCount(id, -count)
    }
}
