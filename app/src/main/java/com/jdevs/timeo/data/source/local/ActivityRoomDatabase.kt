package com.jdevs.timeo.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jdevs.timeo.data.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Activity::class], version = 2, exportSchema = false)
abstract class ActivityRoomDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    companion object {

        @Volatile
        private var INSTANCE: ActivityRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ActivityRoomDatabase {

            return INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context,
                    ActivityRoomDatabase::class.java,
                    "activities"
                )
                    .addCallback(Callback(scope))
                    .build().also {
                        INSTANCE = it
                        return it
                    }
            }
        }
    }

    private class Callback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.activityDao())
                }
            }
        }

        suspend fun populateDatabase(activityDao: ActivityDao) {
            // Delete all content here.
            activityDao.deleteAll()

            // Add sample words.
            activityDao.insert(Activity(name = "Hello"))
            activityDao.insert(Activity(name = "Hello"))
            activityDao.insert(Activity(name = "Hello"))
            activityDao.insert(Activity(name = "Hello"))
            activityDao.insert(Activity(name = "Hello"))
        }
    }
}
