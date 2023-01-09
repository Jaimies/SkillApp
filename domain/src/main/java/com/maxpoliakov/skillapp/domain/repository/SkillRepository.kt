package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillSelectionCriteria
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.coroutines.flow.Flow

interface SkillRepository {
    fun getSkills(): Flow<List<Skill>>
    fun getSkills(criteria: SkillSelectionCriteria): Flow<List<Skill>>

    fun getSkillFlowById(id: Id): Flow<Skill>
    fun getTopSkills(count: Int): Flow<List<Skill>>
    suspend fun getSkillById(id: Id): Skill?

    suspend fun addSkill(skill: Skill): Long
    suspend fun updateName(skillId: Int, newName: String)
    suspend fun updateGoal(skillId: Int, newGoal: Goal?)
    suspend fun deleteSkill(skill: Skill)
    suspend fun updateOrder(skillId: Int, newOrder: Int)
    suspend fun increaseCount(id: Id, count: Long)
    suspend fun decreaseCount(id: Id, count: Long)
}
