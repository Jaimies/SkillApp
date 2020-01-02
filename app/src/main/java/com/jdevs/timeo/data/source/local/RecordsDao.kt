package com.jdevs.timeo.data.source.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.MonthStats
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.WeekStats
import com.jdevs.timeo.util.time.getDaysSinceEpoch
import com.jdevs.timeo.util.time.getMonthSinceEpoch
import com.jdevs.timeo.util.time.getWeeksSinceEpoch

@Dao
interface RecordsDao : BaseDao<Record> {

    @Transaction
    suspend fun insertRecord(record: Record) {

        insert(record)
        registerDayStats(record.time, record.creationDate.getDaysSinceEpoch())
        registerWeekStats(record.time, record.creationDate.getWeeksSinceEpoch())
        registerMonthStats(record.time, record.creationDate.getMonthSinceEpoch())
    }

    @Transaction
    @Query(
        """SELECT records.*, activities.name FROM records
        LEFT JOIN activities ON activities.id = records.activityId 
        ORDER BY records.id DESC"""
    )
    fun getRecords(): DataSource.Factory<Int, Record>

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

    @Query("SELECT * FROM dayStats ORDER BY day DESC")
    fun getDayStats(): DataSource.Factory<Int, DayStats>

    @Query("SELECT * FROM weekStats ORDER BY week DESC")
    fun getWeekStats(): DataSource.Factory<Int, WeekStats>

    @Query("SELECT * FROM monthStats ORDER BY month DESC")
    fun getMonthStats(): DataSource.Factory<Int, MonthStats>
}
