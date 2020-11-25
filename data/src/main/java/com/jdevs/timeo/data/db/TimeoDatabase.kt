package com.jdevs.timeo.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdevs.timeo.data.activities.ActivitiesDao
import com.jdevs.timeo.data.activities.DBActivity
import com.jdevs.timeo.data.records.DBRecord
import com.jdevs.timeo.data.records.RecordsDao
import com.jdevs.timeo.data.stats.DBStatistic
import com.jdevs.timeo.data.stats.StatsDao

@Database(
    entities = [DBActivity::class, DBRecord::class, DBStatistic::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TimeoDatabase : RoomDatabase() {
    abstract fun activitiesDao(): ActivitiesDao
    abstract fun recordsDao(): RecordsDao
    abstract fun statsDao(): StatsDao

    companion object {
        fun create(context: Context) = Room
            .databaseBuilder(context, TimeoDatabase::class.java, "timeo")
            .fallbackToDestructiveMigration()
            .build()
    }
}
