package com.maxpoliakov.skillapp.data.group

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.shared.util.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DBSkillGroupRepository @Inject constructor(
    private val groupDao: GroupDao,
) : SkillGroupRepository {

    override fun getSkillGroups(): Flow<List<SkillGroup>> {
        return groupDao.getGroups().mapList { it.mapToDomain() }
    }

    override fun getSkillGroupFlowById(id: Int): Flow<SkillGroup> {
        return groupDao.getGroupFlowById(id)
            .filterNotNull()
            .map { it.mapToDomain() }
    }

    override suspend fun getSkillGroupById(id: Int): SkillGroup? {
        return groupDao.getGroupById(id)?.mapToDomain()
    }

    override suspend fun addSkillToGroup(skillId: Int, groupId: Int) {
        groupDao.addSkillToGroup(skillId, groupId)
    }

    override suspend fun removeSkillFromGroup(skillId: Int) {
        groupDao.removeSkillFromGroup(skillId)
    }

    override suspend fun createGroup(group: SkillGroup) = groupDao.createGroup(group)

    override suspend fun updateGroupName(groupId: Int, newName: String) {
        groupDao.updateGroupName(groupId, newName)
    }

    override suspend fun updateGoal(groupId: Int, newGoal: Goal?) {
        if (newGoal == null)
            groupDao.updateGoal(groupId, 0, Goal.Type.Daily)
        else
            groupDao.updateGoal(groupId, newGoal.count, newGoal.type)
    }

    override suspend fun updateOrder(groupId: Int, newOrder: Int) {
        groupDao.updateOrder(groupId, newOrder)
    }

    override suspend fun deleteGroup(groupId: Int) {
        groupDao.deleteGroup(groupId)
    }
}
