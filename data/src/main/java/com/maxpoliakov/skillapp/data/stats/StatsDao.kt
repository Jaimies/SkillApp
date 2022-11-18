package com.maxpoliakov.skillapp.data.stats

import androidx.room.Dao
import androidx.room.Query
import com.maxpoliakov.skillapp.data.db.BaseDao
import kotlinx.coroutines.flow.Flow
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
            AND date(date) BETWEEN date(:dateStart) AND date(:dateEnd)
            AND time > 0
            GROUP BY date
        """
    )
    fun getStats(skillId: Int, dateStart: LocalDate, dateEnd: LocalDate): Flow<List<DBStatistic>>

    @Query("SELECT time from stats WHERE skillId = :skillId AND date = :date")
    fun getCountAtDateFlow(skillId: Int, date: LocalDate): Flow<Long?>

    @Query("SELECT time from stats WHERE skillId = :skillId AND date = :date")
    suspend fun getCountAtDate(skillId: Int, date: LocalDate): Long?

    @Query("SELECT time from stats WHERE skillId = :skillId AND date(date) BETWEEN date(:startDate) AND date(:endDate)")
    suspend fun getCountInDateRange(skillId: Int, startDate: LocalDate, endDate: LocalDate): Long?

    @Query("SELECT * FROM stats")
    suspend fun getAllStats(): List<DBStatistic>

    @Query("DELETE FROM stats")
    suspend fun deleteAll()
}
