package com.maxpoliakov.skilapp.data.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.db.MIGRATION_1_2
import com.maxpoliakov.skillapp.data.db.MIGRATION_2_3
import com.maxpoliakov.skillapp.data.db.MIGRATION_3_4
import com.maxpoliakov.skillapp.data.db.MIGRATION_4_5
import com.maxpoliakov.skillapp.data.db.MIGRATION_5_6
import com.maxpoliakov.skillapp.data.getIntValue
import com.maxpoliakov.skillapp.data.getLongValue
import com.maxpoliakov.skillapp.data.getStringValue
import com.maxpoliakov.skillapp.data.queryASingleItem
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Duration

class MigrationsTest {
    private val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
    )

    @Test
    fun migration_from_1_to_2() = runBlocking {
        val db = helper.createDatabase(AppDatabase.DATABASE_NAME, 1)

        db.execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate)
                VALUES("Name", 40, 10, 0, "1970-01-01")
            """)

        db.execSQL("""INSERT INTO records (time, skillId, recordName, date)
                VALUES (10, 1, "", "1970-01-01")
            """)

        db.execSQL("""INSERT INTO stats (date, skillId, time) 
                VALUES("1970-01-01", 1, 15)
            """)

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 2, true, MIGRATION_1_2)

        db.queryASingleItem("SELECT totalTime, initialTime FROM skills WHERE id = 1") { cursor ->
            cursor.getLongValue("totalTime") shouldBe Duration.ofMinutes(40).toMillis()
            cursor.getLongValue("initialTime") shouldBe Duration.ofMinutes(10).toMillis()
        }

        db.queryASingleItem("SELECT time FROM records WHERE id = 1") { cursor ->
            cursor.getLongValue("time") shouldBe Duration.ofMinutes(10).toMillis()
        }

        db.queryASingleItem("SELECT time FROM stats WHERE skillId = 1") { cursor ->
            cursor.getLongValue("time") shouldBe Duration.ofMinutes(15).toMillis()
        }
    }

    @Test
    fun migration_from_2_to_3() = runBlocking {
        val db = helper.createDatabase(AppDatabase.DATABASE_NAME, 2)

        db.execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate)
                VALUES ("name", 100000, 1000, 100, "1970-01-01")
            """)

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 3, true, MIGRATION_2_3)

        db.queryASingleItem("SELECT `order`, name, totalTime FROM skills") { cursor ->
            cursor.getIntValue("order") shouldBe -1
            cursor.getStringValue("name") shouldBe "name"
            cursor.getLongValue("totalTime") shouldBe 100_000
        }
    }

    @Test
    fun migration_from_3_to_4() = runBlocking {
        val db = helper.createDatabase(AppDatabase.DATABASE_NAME, 3)

        db.execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate, `order`)
                VALUES ("name", 100000, 1000, 100, "1970-01-01", 0)
            """)

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 4, true, MIGRATION_3_4)

        db.queryASingleItem("SELECT groupId FROM skills WHERE id = 1") { cursor ->
            cursor.getIntValue("groupId") shouldBe -1
        }
    }

    @Test
    fun migration_from_4_to_5() = runBlocking {
        val db = helper.createDatabase(AppDatabase.DATABASE_NAME, 4)
        db.execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate, `order`, groupId)
                VALUES ("name", 100000, 1000, 100, "1970-01-01", 0, 2)
            """)

        db.execSQL("""INSERT INTO groups(id, name, `order`) VALUES(1, "name", 0)""")

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 5, true, MIGRATION_4_5)

        db.queryASingleItem("SELECT goalTime, goalType FROM skills WHERE id = 1") { cursor ->
            cursor.getLongValue("goalTime") shouldBe 0
            cursor.getStringValue("goalType") shouldBe "Daily"
        }

        db.queryASingleItem("SELECT goalTime, goalType FROM groups WHERE id = 1") { cursor ->
            cursor.getLongValue("goalTime") shouldBe 0
            cursor.getStringValue("goalType") shouldBe "Daily"
        }
    }

    @Test
    fun migration_5_to_6() = runBlocking<Unit> {
        val db = helper.createDatabase(AppDatabase.DATABASE_NAME, 5)

        db.execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate, `order`, groupId, goalType, goalTime)
                VALUES ("name", 100000, 1000, 100, "1970-01-01", 0, 2, "Daily", 100000)
            """)

        db.execSQL("""INSERT INTO stats (date, skillId, time) 
                VALUES("1970-01-01", 1, 15)
            """)

        db.execSQL("""INSERT INTO records (time, skillId, recordName, date)
                VALUES (10, 1, "", "1970-01-01")
            """)

        db.execSQL("""INSERT INTO groups(id, name, `order`, goalType, goalTime) 
            VALUES(1, "name", 0, "Daily", 200000)
            """)

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 6, true, MIGRATION_5_6)
    }
}
