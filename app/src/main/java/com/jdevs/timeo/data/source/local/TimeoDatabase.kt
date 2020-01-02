package com.jdevs.timeo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.MonthStats
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.WeekStats

@Database(
    entities = [Activity::class, Record::class, DayStats::class, WeekStats::class, MonthStats::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TimeoDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun recordsDao(): RecordsDao
}
