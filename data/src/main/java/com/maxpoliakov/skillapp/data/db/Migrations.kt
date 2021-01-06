package com.maxpoliakov.skillapp.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val MILLIS_IN_SECOND = 60000

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """UPDATE skills SET 
                initialTime = initialTime * $MILLIS_IN_SECOND,
                totalTime = totalTime * $MILLIS_IN_SECOND"""
        )

        database.execSQL(
            """UPDATE records SET time = time * $MILLIS_IN_SECOND"""
        )

        database.execSQL(
            """UPDATE stats SET time = time * $MILLIS_IN_SECOND"""
        )
    }
}
