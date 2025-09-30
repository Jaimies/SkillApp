package com.theskillapp.skillapp.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.theskillapp.skillapp.data.group.DBGroup
import com.theskillapp.skillapp.data.group.GroupDao
import com.theskillapp.skillapp.data.records.DBRecord
import com.theskillapp.skillapp.data.records.RecordsDao
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.skill.SkillDao
import com.theskillapp.skillapp.data.stats.DBStatistic
import com.theskillapp.skillapp.data.stats.StatsDao
import com.theskillapp.skillapp.data.timer.DBTimer
import com.theskillapp.skillapp.data.timer.TimerDao

@Database(
    entities = [DBSkill::class, DBRecord::class, DBStatistic::class, DBGroup::class, DBTimer::class],
    version = 8,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 6,
            to = 7
        ),
        AutoMigration(
            from = 7,
            to = 8,
        ),
    ],
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun skillDao(): SkillDao
    abstract fun recordsDao(): RecordsDao
    abstract fun statsDao(): StatsDao
    abstract fun skillGroupDao(): GroupDao
    abstract fun timerDao(): TimerDao

    companion object {
        fun create(context: Context) = Room
            .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
            .build()

        const val DATABASE_NAME = "main"
    }
}
