package com.maxpoliakov.skillapp.data.skill

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import kotlinx.coroutines.flow.Flow
import java.time.Duration

@Dao
interface SkillDao : BaseDao<DBSkill> {
    @Query("SELECT * FROM skills ORDER BY `order` ASC, id DESC")
    fun getSkills(): Flow<List<DBSkill>>

    @Query("SELECT * FROM skills")
    suspend fun getAllSkills(): List<DBSkill>

    @Query("SELECT * FROM skills WHERE unit = 'Millis' ORDER BY totalTime DESC LIMIT :count")
    fun getTopSkills(count: Int): Flow<List<DBSkill>>

    @Query("""SELECT * FROM skills WHERE id = :id""")
    fun getSkillFlow(id: Int): Flow<DBSkill?>

    @Query("SELECT * FROM skills WHERE id = :id")
    fun getSkill(id: Int): DBSkill?

    @Query("UPDATE skills SET name = :name WHERE id = :id")
    suspend fun updateName(id: Int, name: String)

    @Query("UPDATE skills SET goalTime = :goalCount, goalType = :goalType WHERE id = :id")
    suspend fun updateGoal(id: Int, goalCount: Long, goalType: Goal.Type)

    @Query("UPDATE skills SET `order` = :order WHERE id = :id")
    suspend fun setOrder(id: Int, order: Int)

    @Query("UPDATE skills SET totalTime = totalTime + :by WHERE id = :id")
    suspend fun increaseCount(id: Int, by: Long)

    @Query("DELETE FROM skills")
    suspend fun deleteAll()
}
