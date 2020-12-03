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
        """
        SELECT skills.*, (
            SELECT SUM(time) FROM records WHERE skillId = :id
            AND date(records.date) >= date('now','localtime', '-6 day')
            AND date(records.date) <= date('now', 'localtime')
        ) as lastWeekTime FROM skills
        WHERE skills.id = :id"""
    )
    fun getSkill(id: Int): Flow<DBSkill?>

    @Query("UPDATE skills SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Long)
}
