package com.jdevs.timeo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdevs.timeo.data.db.model.DBActivity
import com.jdevs.timeo.data.db.model.DBDayStats
import com.jdevs.timeo.data.db.model.DBMonthStats
import com.jdevs.timeo.data.db.model.DBProject
import com.jdevs.timeo.data.db.model.DBRecord
import com.jdevs.timeo.data.db.model.DBWeekStats

@Database(
    entities = [DBActivity::class, DBProject::class, DBRecord::class, DBDayStats::class, DBWeekStats::class, DBMonthStats::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TimeoDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun projectsDao(): ProjectsDao
    abstract fun recordsDao(): RecordsDao
    abstract fun statsDao(): StatsDao
}
