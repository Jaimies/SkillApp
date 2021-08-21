package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.coroutines.flow.Flow

interface SkillGroupRepository {
    fun getSkillGroups(): Flow<List<SkillGroup>>
    fun getSkillGroupFlowById(id: Int): Flow<SkillGroup>

    suspend fun getSkillGroupById(id: Int): SkillGroup?
    suspend fun addSkillToGroup(skillId: Int, groupId: Int)
    suspend fun removeSkillFromGroup(skillId: Int)

    suspend fun createGroup(name: String, skills: List<Skill>)
    suspend fun updateGroupName(groupId: Int, newName: String)
    suspend fun updateOrder(groupId: Int, newOrder: Int)
    suspend fun deleteGroup(groupId: Int)
}
