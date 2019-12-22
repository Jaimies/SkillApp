package com.jdevs.timeo.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jdevs.timeo.data.Activity

@Database(entities = [Activity::class], version = 2, exportSchema = false)
abstract class ActivitiesDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao
}
