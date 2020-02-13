package com.jdevs.timeo.data.stats

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface StatsDao {

    @Query(
        """INSERT OR REPLACE INTO dayStats (time, day) VALUES(
                COALESCE((
                    (SELECT time FROM dayStats WHERE day = :day) + :time), 
                :time), :day)
            """
    )
    fun registerDayStats(time: Long, day: Long)

    @Query(
        """INSERT OR REPLACE INTO weekStats (time, week) VALUES(
                COALESCE((
                  (SELECT time FROM weekStats WHERE week = :week) + :time), 
                :time), :week)
                """
    )
    fun registerWeekStats(time: Long, week: Int)

    @Query(
        """INSERT OR REPLACE INTO monthStats (time, month) VALUES(
                COALESCE((
                  (SELECT time FROM monthStats WHERE month = :month) + :time), 
                :time), :month)
                """
    )
    fun registerMonthStats(time: Long, month: Short)

    @Query(
        """
        SELECT * FROM dayStats
        WHERE date(day * 86400, 'unixepoch') > date('now', '-7 day') 
        AND date(day * 86400, 'unixepoch') <= date('now') 
        AND time > 0 
        ORDER BY day LIMIT 7"""
    )
    fun getDayStats(): LiveData<List<DBDayStats>>

    @Query("SELECT * FROM weekStats ORDER BY week LIMIT 7")
    fun getWeekStats(): LiveData<List<DBWeekStats>>

    @Query("SELECT * FROM monthStats ORDER BY month LIMIT 7")
    fun getMonthStats(): LiveData<List<DBMonthStats>>
}
