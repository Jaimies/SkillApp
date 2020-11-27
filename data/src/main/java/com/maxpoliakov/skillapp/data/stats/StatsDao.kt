package com.maxpoliakov.skillapp.data.stats

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {
    @Query(
        """INSERT OR REPLACE INTO stats (time, day, activityId) VALUES(
                COALESCE((
                    (SELECT time FROM stats WHERE day = :day AND activityId = :activityId) + :time), 
                :time), :day, :activityId)
            """
    )
    suspend fun addRecord(activityId: Int, day: Long, time: Long)

    @Query(
        """
        SELECT * FROM stats
        WHERE activityId = :activityId
        AND date(day * 86400, 'unixepoch') > date('now', '-14 day') 
        AND date(day * 86400, 'unixepoch') <= date('now') 
        AND time > 0
        """
    )
    fun getStats(activityId: Int): Flow<List<DBStatistic>>
}
