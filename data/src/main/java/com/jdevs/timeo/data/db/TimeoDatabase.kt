package com.jdevs.timeo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdevs.timeo.data.activities.ActivitiesDao
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.data.projects.DBProject
import com.jdevs.timeo.data.projects.ProjectsDao
import com.jdevs.timeo.data.records.DBRecord
import com.jdevs.timeo.data.records.RecordsDao
import com.jdevs.timeo.data.stats.DBDayStats
import com.jdevs.timeo.data.stats.DBMonthStats
import com.jdevs.timeo.data.stats.DBWeekStats
import com.jdevs.timeo.data.stats.StatsDao

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

    companion object {

        private const val DATABASE_NAME = "timeo"

        fun create(context: Context): TimeoDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                TimeoDatabase::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
