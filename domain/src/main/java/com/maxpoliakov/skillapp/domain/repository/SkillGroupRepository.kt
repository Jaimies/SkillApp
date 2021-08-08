package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.coroutines.flow.Flow

interface SkillGroupRepository {
    fun getSkillGroups(): Flow<List<SkillGroup>>

    suspend fun createGroup(name: String, skills: List<Skill>)
}
