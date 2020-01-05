package com.jdevs.timeo.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.MonthStats
import com.jdevs.timeo.data.WeekStats

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

    @Query("SELECT * FROM dayStats WHERE time > 0 ORDER BY day DESC")
    fun getDayStats(): DataSource.Factory<Int, DayStats>

    @Query("SELECT * FROM weekStats WHERE time > 0  ORDER BY week DESC")
    fun getWeekStats(): DataSource.Factory<Int, WeekStats>

    @Query("SELECT * FROM monthStats WHERE time > 0 ORDER BY month DESC")
    fun getMonthStats(): DataSource.Factory<Int, MonthStats>
}
