package com.maxpoliakov.skilapp.data.db

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.db.MIGRATION_1_2
import com.maxpoliakov.skillapp.data.db.MIGRATION_2_3
import com.maxpoliakov.skillapp.data.db.MIGRATION_3_4
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.test.await
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.Duration
import java.time.LocalDate

class MigrationsTest {
    private val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    private val date = LocalDate.ofEpochDay(0)
    private val skill = DBSkill(
        1, "Name", totalTime = Duration.ofMinutes(40), initialTime = Duration.ofMinutes(10), creationDate = date
    )
    private val record = DBRecord(id = 1, skillId = 1, time = Duration.ofMinutes(10), date = date)
    private val statistic = DBStatistic(date, 1, Duration.ofMinutes(15))

    @Test
    fun migration_from_1_to_2() = runBlocking {
        helper.createDatabase(AppDatabase.DATABASE_NAME, 1).apply {
            execSQL(
                """INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate)
                VALUES("${skill.name}", ${skill.totalTime.toMinutes()}, ${skill.initialTime.toMinutes()}, 0, "1970-01-01")
            """
            )
            execSQL(
                """INSERT INTO records (time, skillId, recordName, date)
                VALUES (${record.time.toMinutes()}, ${record.skillId}, "${record.recordName}", "1970-01-01")
            """
            )

            execSQL(
                """INSERT INTO stats (date, skillId, time) 
                VALUES("1970-01-01", 1, ${statistic.time.toMinutes()})
            """
            )
        }

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 2, true, MIGRATION_1_2)

        val roomDb = AppDatabase.create(InstrumentationRegistry.getInstrumentation().targetContext)

        roomDb.skillDao().getSkill(1) shouldBe skill
        roomDb.statsDao().getTimeAtDate(statistic.date) shouldBe statistic.time
        roomDb.recordsDao().getRecordById(1) shouldBe record
    }

    @Test
    fun migration_from_2_to_3() = runBlocking {
        helper.createDatabase(AppDatabase.DATABASE_NAME, 2).apply {
            execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate)
                VALUES ("name", 100000, 1000, 100, "1970-01-01")
            """)
        }

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 3, true, MIGRATION_2_3)

        val roomDb = AppDatabase.create(InstrumentationRegistry.getInstrumentation().targetContext)
        val skill = roomDb.skillDao().getSkill(1)!!
        skill.order shouldBe -1
        skill.name shouldBe "name"
        skill.totalTime shouldBe Duration.ofMillis(100000)
    }

    @Test
    fun migration_from_3_to_4() = runBlocking {
        helper.createDatabase(AppDatabase.DATABASE_NAME, 3).apply {
            execSQL("""INSERT INTO skills (name, totalTime, initialTime, lastWeekTime, creationDate, `order`)
                VALUES ("name", 100000, 1000, 100, "1970-01-01", 0)
          """)
        }

        helper.runMigrationsAndValidate(AppDatabase.DATABASE_NAME, 4, true, MIGRATION_3_4)

        val roomDb = AppDatabase.create(InstrumentationRegistry.getInstrumentation().targetContext)
        val skill = roomDb.skillDao().getSkill(1)!!

        skill.groupId shouldBe -1
    }
}
