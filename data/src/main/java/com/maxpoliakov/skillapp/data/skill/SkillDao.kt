package com.maxpoliakov.skillapp.data.skill

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao : BaseDao<DBSkill> {
    @Query("SELECT * FROM skills ORDER BY totalTime DESC")
    fun getSkills(): Flow<List<DBSkill>>

    @Query(
        """SELECT skills.*, SUM(records.time) as lastWeekTime FROM skills
        LEFT JOIN records ON skillId = skills.id
        AND date(records.timestamp, 'localtime') > date('now','localtime', '-6 day')
        AND date(records.timestamp, 'localtime') <= date('now', 'localtime')
        WHERE skills.id = :id"""
    )
    fun getSkill(id: Int): Flow<DBSkill>

    @Query("UPDATE skills SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Long)
}
