package com.maxpoliakov.skillapp.data.group

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.maxpoliakov.skillapp.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao : BaseDao<DBGroup> {
    @Transaction
    @Query("SELECT * from groups")
    fun getGroups(): Flow<List<GroupWithSkills>>

    @Transaction
    @Query("SELECT * FROM groups WHERE id = :id")
    fun getGroupById(id: Int): Flow<GroupWithSkills>

    @Query("UPDATE skills SET groupId = :groupId WHERE id = :skillId ")
    suspend fun addSkillToGroup(skillId: Int, groupId: Int)

    @Query("UPDATE groups SET name = :newName WHERE id = :groupId")
    suspend fun updateGroupName(groupId: Int, newName: String)

    @Transaction
    suspend fun createGroup(name: String, skillIds: List<Int>) {
        val groupId = this.insert(DBGroup(name = name))

        skillIds.forEach { skillId ->
            this.addSkillToGroup(skillId, groupId.toInt())
        }
    }
}
