package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Id
import kotlinx.coroutines.flow.Flow
import java.time.Duration

interface SkillRepository {
    fun getSkills(): Flow<List<Skill>>

    fun getSkillById(id: Id): Flow<Skill>

    suspend fun addSkill(skill: Skill): Long
    suspend fun saveSkill(skill: Skill)
    suspend fun deleteSkill(skill: Skill)
    suspend fun setOrder(id: Int, order: Int)
    suspend fun increaseTime(id: Id, time: Duration)
    suspend fun decreaseTime(id: Id, time: Duration)
}
