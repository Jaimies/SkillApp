package com.jdevs.timeo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.RecordStats

@Database(
    entities = [Activity::class, Record::class, RecordStats::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TimeoDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun recordsDao(): RecordsDao
}
