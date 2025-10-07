package com.theskillapp.skillapp.data.backup

import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.group.DBGroup
import com.theskillapp.skillapp.data.records.DBRecord
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.stats.DBStatistic
import com.theskillapp.skillapp.domain.model.BackupData
import com.theskillapp.skillapp.domain.repository.BackupCreator
import com.theskillapp.skillapp.shared.util.setClock
import com.theskillapp.skillapp.test.clockOfInstant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltAndroidTest
class DBBackupRestorerTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var backupRestorer: DBBackupRestorer

    @Inject
    lateinit var backupCreator: DBBackupCreator

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun teardown() {
        setClock(Clock.systemDefaultZone())
    }

    @Test
    fun restoresBackup_onADifferentDateFromWhenItWasCreated() = runBlocking {
        db.skillDao().insert(skills)
        db.recordsDao().insert(records)
        db.statsDao().insert(statistics)
        db.skillGroupDao().insert(groups)

        val backupResult = backupCreator.create() as BackupCreator.Result.Success

        db.skillDao().deleteAll()
        db.recordsDao().deleteAll()
        db.skillGroupDao().deleteAll()
        db.statsDao().deleteAll()

        setClock(clockOfInstant(Instant.now().plus(1, ChronoUnit.DAYS)))
        backupRestorer.restore(backupResult.data)

        db.skillDao().getAllSkills() shouldBe skills
        db.recordsDao().getAllRecords() shouldBe records
        db.statsDao().getAllStats() shouldBe statistics
        db.skillGroupDao().getAllGroups() shouldBe groups
    }

    @Test
    fun restoresBackupFromOldDbVersion() = runBlocking {
        val backup = """{"skills":[{"id":1,"name":"Skill 0"},{"id":2,"name":"Skill 1","totalTime":3600000,"initialTime":60000},{"id":3,"name":"Skill 2","totalTime":7200000,"initialTime":120000},{"id":4,"name":"Skill 3","totalTime":10800000,"initialTime":180000},{"id":5,"name":"Skill 4","totalTime":14400000,"initialTime":240000}],"records":[{"id":1,"skillId":1},{"id":2,"time":3600000,"skillId":2},{"id":3,"time":7200000,"skillId":3},{"id":4,"time":10800000,"skillId":4},{"id":5,"time":14400000,"skillId":5}],"stats":[{"date":"2022-04-30","skillId":1,"time":0},{"date":"2022-04-30","skillId":2,"time":3600000},{"date":"2022-04-30","skillId":3,"time":7200000},{"date":"2022-04-30","skillId":4,"time":10800000},{"date":"2022-04-30","skillId":5,"time":14400000}],"groups":[{"id":1,"name":"Group 0","order":0},{"id":2,"name":"Group 1","order":1},{"id":3,"name":"Group 2","order":2},{"id":4,"name":"Group 3","order":3},{"id":5,"name":"Group 4","order":4}]}"""

        backupRestorer.restore(BackupData(backup))

        db.skillDao().getAllSkills() shouldBe skills
        db.recordsDao().getAllRecords() shouldBe records
        db.statsDao().getAllStats() shouldBe statistics
        db.skillGroupDao().getAllGroups() shouldBe groups
    }

    companion object {
        val skills = List(5) { index ->
            DBSkill(
                id = index + 1,
                name = "Skill $index",
                totalTime = Duration.ofHours(index.toLong()).toMillis(),
                initialTime = Duration.ofMinutes(index.toLong()).toMillis(),
            )
        }

        val records = List(5) { index ->
            DBRecord(
                id = index + 1,
                time = Duration.ofHours(index.toLong()).toMillis(),
                skillId = index + 1,
            )
        }

        val statistics = List(5) { index ->
            DBStatistic(
                skillId = index + 1,
                time = Duration.ofHours(index.toLong()).toMillis(),
                date = LocalDate.parse("2022-04-30"),
            )
        }

        val groups = List(5) { index ->
            DBGroup(id = index + 1, name = "Group $index", order = index)
        }
    }
}
