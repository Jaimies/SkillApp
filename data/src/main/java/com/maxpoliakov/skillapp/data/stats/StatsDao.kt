package com.maxpoliakov.skillapp.data.stats

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import kotlinx.coroutines.flow.Flow
import java.time.Duration
import java.time.LocalDate

@Dao
interface StatsDao : BaseDao<DBStatistic> {
    @Query(
        """INSERT OR REPLACE INTO stats (time, date, skillId) VALUES(
                COALESCE((
                    (SELECT time FROM stats WHERE date = :date AND skillId = :skillId) + :count), 
                :count), :date, :skillId)
            """
    )
    suspend fun addRecord(skillId: Int, date: LocalDate, count: Long)

    @Query(
        """
        SELECT date, :skillId as skillId, SUM(time) as time FROM stats
        WHERE (:skillId = -1 OR skillId = :skillId)
        AND date(date) > date('now','localtime', '-' || :daysAgoStart || ' days') 
        AND date(date) <= date('now', 'localtime')
        AND time > 0
        GROUP BY date
        """
    )
    fun getStats(skillId: Int, daysAgoStart: Long): Flow<List<DBStatistic>>

    @Query("SELECT time from stats WHERE skillId = :skillId AND date = :date")
    fun getCountAtDate(skillId: Int, date: LocalDate): Flow<Long?>

    @Query("SELECT SUM(time) as time FROM stats WHERE date = :date")
    suspend fun getCountAtDate(date: LocalDate): Long

    @Query("SELECT * FROM stats")
    suspend fun getAllStats(): List<DBStatistic>

    @Query("DELETE FROM stats")
    suspend fun deleteAll()
}
