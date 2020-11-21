package com.jdevs.timeo.data.stats

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatsDao {

    @Query(
        """INSERT OR REPLACE INTO dayStats (time, day) VALUES(
                COALESCE((
                    (SELECT time FROM dayStats WHERE day = :day) + :time), 
                :time), :day)
            """
    )
    suspend fun addDayRecord(day: Int, time: Int)

    @Query(
        """INSERT OR REPLACE INTO weekStats (time, week) VALUES(
                COALESCE((
                  (SELECT time FROM weekStats WHERE week = :week) + :time), 
                :time), :week)
                """
    )
    suspend fun addWeekRecord(week: Int, time: Int)

    @Query(
        """INSERT OR REPLACE INTO monthStats (time, month) VALUES(
                COALESCE((
                  (SELECT time FROM monthStats WHERE month = :month) + :time), 
                :time), :month)
                """
    )
    suspend fun addMonthRecord(month: Int, time: Int)

    @Query(
        """
        SELECT * FROM dayStats
        WHERE date(day * 86400, 'unixepoch') > date('now', '-7 day') 
        AND date(day * 86400, 'unixepoch') <= date('now') 
        AND time > 0 
        ORDER BY day LIMIT 7"""
    )
    fun getDayStats(): Flow<List<DBDayStats>>

    @Query(
        """
        SELECT * FROM weekStats
        WHERE date(0, 'unixepoch', (week * 7) || ' day') > date('now', '-49 day') 
        AND date(0, 'unixepoch', (week * 7) || ' day') <= date('now')
        AND time > 0 
        ORDER BY week LIMIT 7"""
    )
    fun getWeekStats(): Flow<List<DBWeekStats>>

    @Query(
        """
        SELECT * FROM monthStats
        WHERE date(0, 'unixepoch', month || ' month') > date('now', '-7 month') 
        AND date(0, 'unixepoch', month || ' month') <= date('now') 
        AND time > 0 
        ORDER BY month LIMIT 7"""
    )
    fun getMonthStats(): Flow<List<DBMonthStats>>
}
