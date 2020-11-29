package com.maxpoliakov.skillapp.data.stats

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {
    @Query(
        """INSERT OR REPLACE INTO stats (time, day, skillId) VALUES(
                COALESCE((
                    (SELECT time FROM stats WHERE day = :day AND skillId = :skillId) + :time), 
                :time), :day, :skillId)
            """
    )
    suspend fun addRecord(skillId: Int, day: Long, time: Long)

    @Query(
        """
        SELECT day, :skillId as skillId, SUM(time) as time FROM stats
        WHERE :skillId = -1 OR skillId = :skillId
        AND date(day * 86400, 'unixepoch') > date('now','localtime', '-14 day') 
        AND date(day * 86400, 'unixepoch') <= date('now', 'localtime')
        AND time > 0
        GROUP BY day
        """
    )
    fun getStats(skillId: Int): Flow<List<DBStatistic>>
}
