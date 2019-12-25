package com.jdevs.timeo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record

@Database(entities = [Activity::class, Record::class], version = 1, exportSchema = false)
abstract class TimeoDatabase : RoomDatabase() {

    abstract fun activitiesDao(): ActivitiesDao
    abstract fun recordsDao(): RecordsDao
}
