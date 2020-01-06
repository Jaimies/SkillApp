package com.jdevs.timeo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.domain.model.MonthStats
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.model.WeekStats

@Database(
    entities = [Activity::class, Record::class, DayStats::class, WeekStats::class, MonthStats::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TimeoDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun recordsDao(): RecordsDao
    abstract fun statsDao(): StatsDao
}
