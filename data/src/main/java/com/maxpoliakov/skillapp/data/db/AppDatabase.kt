package com.maxpoliakov.skillapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maxpoliakov.skillapp.data.activities.ActivitiesDao
import com.maxpoliakov.skillapp.data.activities.DBActivity
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.data.stats.StatsDao

@Database(
    entities = [DBActivity::class, DBRecord::class, DBStatistic::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activitiesDao(): ActivitiesDao
    abstract fun recordsDao(): RecordsDao
    abstract fun statsDao(): StatsDao

    companion object {
        fun create(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, "timeo")
            .build()
    }
}
