package com.maxpoliakov.skillapp.data.timer

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao: BaseDao<DBTimer> {
    @Query("SELECT * FROM timers")
    fun getAll(): Flow<List<DBTimer>>

    @Query("DELETE FROM timers WHERE skillId = :skillId")
    fun deleteBySkillId(skillId: Int)
}
