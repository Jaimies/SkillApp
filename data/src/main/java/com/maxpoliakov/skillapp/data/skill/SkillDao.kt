package com.maxpoliakov.skillapp.data.skill

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import kotlinx.coroutines.flow.Flow
import java.time.Duration

@Dao
interface SkillDao : BaseDao<DBSkill> {
    @Query("SELECT * FROM skills ORDER BY `order` ASC, id DESC")
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

    @Query("UPDATE skills SET name = :name WHERE id = :id")
    suspend fun updateName(id: Int, name: String)

    @Query("UPDATE skills SET `order` = :order WHERE id = :id")
    suspend fun setOrder(id: Int, order: Int)

    @Query("UPDATE skills SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseTime(id: Int, by: Duration)
}
