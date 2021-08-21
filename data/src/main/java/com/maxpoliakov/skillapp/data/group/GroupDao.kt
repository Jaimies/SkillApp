package com.maxpoliakov.skillapp.data.group

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.maxpoliakov.skillapp.data.db.BaseDao
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao : BaseDao<DBGroup> {
    @Transaction
    @Query("SELECT * from groups")
    fun getGroups(): Flow<List<GroupWithSkills>>

    @Transaction
    @Query("SELECT * FROM groups WHERE id = :id")
    fun getGroupFlowById(id: Int): Flow<GroupWithSkills>

    @Transaction
    @Query("SELECT * FROM groups WHERE id = :id")
    suspend fun getGroupById(id: Int): GroupWithSkills?

    @Query("UPDATE skills SET groupId = :groupId WHERE id = :skillId ")
    suspend fun addSkillToGroup(skillId: Int, groupId: Int)

    @Query("UPDATE skills SET groupId = -1 WHERE id = :skillId")
    suspend fun removeSkillFromGroup(skillId: Int)

    @Query("UPDATE groups SET name = :newName WHERE id = :groupId")
    suspend fun updateGroupName(groupId: Int, newName: String)

    @Query("UPDATE groups SET `order` = :newOrder WHERE id = :groupId")
    suspend fun updateOrder(groupId: Int, newOrder: Int)

    @Query("DELETE FROM groups WHERE id = :groupId")
    suspend fun deleteGroup(groupId: Int)

    @Transaction
    suspend fun createGroup(group: SkillGroup) {
        val groupId = this.insert(DBGroup(name = group.name, order = group.order))

        group.skills.forEach { skill ->
            this.addSkillToGroup(skill.id, groupId.toInt())
        }
    }
}
