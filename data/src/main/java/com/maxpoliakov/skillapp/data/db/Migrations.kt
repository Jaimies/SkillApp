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

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """ALTER TABLE skills
                ADD 'order' INTEGER NOT NULL
                DEFAULT(-1)
            """
        )
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE skills 
            ADD groupId INTEGER NOT NULL
            DEFAULT(-1)
        """)

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS groups(id INTEGER NOT NULL PRIMARY KEY, name TEXT NOT NULL, `order` INT NOT NULL)
        """)
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            ALTER TABLE skills 
            ADD timeTarget INTEGER NOT NULL
            DEFAULT(0)
        """)

        database.execSQL("""
            ALTER TABLE skills 
            ADD targetInterval TEXT NOT NULL
            DEFAULT('Daily')
        """)
    }
}
