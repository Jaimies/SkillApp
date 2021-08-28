package com.maxpoliakov.skillapp.data.stats

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDate

@Dao
interface StatsDao {
    @Query(
        """INSERT OR REPLACE INTO stats (time, date, skillId) VALUES(
                COALESCE((
                    (SELECT time FROM stats WHERE date = :date AND skillId = :skillId) + :time), 
                :time), :date, :skillId)
            """
    )
    suspend fun addRecord(skillId: Int, date: LocalDate, time: Duration)

    @Query(
        """
        SELECT date, :skillId as skillId, SUM(time) as time FROM stats
        WHERE (:skillId = -1 OR skillId = :skillId)
        AND date(date) > date('now','localtime', '-7 day') 
        AND date(date) <= date('now', 'localtime')
        AND time > 0
        GROUP BY date
        """
    )
    fun getStats(skillId: Int): Flow<List<DBStatistic>>

    @Query("SELECT SUM(time) as time FROM stats WHERE date = :date")
    suspend fun getTimeAtDate(date: LocalDate): Duration

    @Query("SELECT * FROM stats")
    suspend fun getAllStats(): List<DBStatistic>
}
