package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.coroutines.flow.Flow

interface SkillGroupRepository {
    fun getSkillGroups(): Flow<List<SkillGroup>>
    fun getSkillGroupById(id: Int): Flow<SkillGroup>

    suspend fun addSkillToGroup(skillId: Int, groupId: Int)
    suspend fun createGroup(name: String, skills: List<Skill>)
    suspend fun updateGroupName(groupId: Int, newName: String)
}
