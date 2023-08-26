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
            SELECT date, 0 AS skillId, SUM(time) as time FROM stats
            WHERE skillId IN (:skillIds)
            AND date(date) BETWEEN date(:dateStart) AND date(:dateEnd)
            AND time > 0
            GROUP BY date
        """
    )
    fun getStatsGroupedByDate(skillIds: List<Int>, dateStart: LocalDate, dateEnd: LocalDate): Flow<List<DBStatistic>>

    @Query("SELECT SUM(time) FROM stats WHERE skillId IN (:skillIds) AND date(date) BETWEEN date(:startDate) AND date(:endDate)")
    fun getCountInDateRange(skillIds: List<Int>, startDate: LocalDate, endDate: LocalDate): Flow<Long?>

    @Query("SELECT * FROM stats")
    suspend fun getAllStats(): List<DBStatistic>

    @Query("DELETE FROM stats")
    suspend fun deleteAll()
}
