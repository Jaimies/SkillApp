package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Id
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface SkillRepository {
    fun getSkills(): Flow<List<Skill>>

    fun getSkillFlowById(id: Id): Flow<Skill>
    fun getTopSkills(count: Int): Flow<List<Skill>>
    suspend fun getSkillById(id: Id): Skill?

    suspend fun addSkill(skill: Skill): Long
    suspend fun updateName(skillId: Int, newName: String)
    suspend fun deleteSkill(skill: Skill)
    suspend fun updateOrder(skillId: Int, newOrder: Int)
    suspend fun increaseTime(id: Id, time: Duration)
    suspend fun decreaseTime(id: Id, time: Duration)
}
