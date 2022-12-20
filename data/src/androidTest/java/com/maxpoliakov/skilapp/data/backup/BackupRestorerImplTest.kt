package com.maxpoliakov.skilapp.data.backup

import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.backup.DBBackupCreator
import com.maxpoliakov.skillapp.data.backup.BackupRestorerImpl
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.DBGroup
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDate

class BackupRestorerImplTest {
    private lateinit var db: AppDatabase
    private lateinit var backupRestorer: BackupRestorerImpl
    private lateinit var backupCreator: DBBackupCreator

    @Before
    fun setup() {
        db = createTestDatabase()
        backupCreator = DBBackupCreator(db)
        backupRestorer = BackupRestorerImpl(db)
    }

    @Test
    fun restoresBackup() = runBlocking {
        db.skillDao().insert(skills)
        db.recordsDao().insert(records)
        db.statsDao().insert(statistics)
        db.skillGroupDao().insert(groups)

        val backup = backupCreator.create()

        db.skillDao().deleteAll()
        db.recordsDao().deleteAll()
        db.skillGroupDao().deleteAll()
        db.statsDao().deleteAll()

        backupRestorer.restore(backup)

        db.skillDao().getAllSkills() shouldBe skills
        db.recordsDao().getAllRecords() shouldBe records
        db.statsDao().getAllStats() shouldBe statistics
        db.skillGroupDao().getAllGroups() shouldBe groups
    }

    @Test
    fun restoresBackupFromOldDbVersion() = runBlocking {
        val backup = """{"skills":[{"id":1,"name":"Skill 0"},{"id":2,"name":"Skill 1","totalTime":3600000,"initialTime":60000},{"id":3,"name":"Skill 2","totalTime":7200000,"initialTime":120000},{"id":4,"name":"Skill 3","totalTime":10800000,"initialTime":180000},{"id":5,"name":"Skill 4","totalTime":14400000,"initialTime":240000}],"records":[{"id":1,"skillId":1},{"id":2,"time":3600000,"skillId":2},{"id":3,"time":7200000,"skillId":3},{"id":4,"time":10800000,"skillId":4},{"id":5,"time":14400000,"skillId":5}],"stats":[{"date":"2022-04-30","skillId":1,"time":0},{"date":"2022-04-30","skillId":2,"time":3600000},{"date":"2022-04-30","skillId":3,"time":7200000},{"date":"2022-04-30","skillId":4,"time":10800000},{"date":"2022-04-30","skillId":5,"time":14400000}],"groups":[{"id":1,"name":"Group 0","order":0},{"id":2,"name":"Group 1","order":1},{"id":3,"name":"Group 2","order":2},{"id":4,"name":"Group 3","order":3},{"id":5,"name":"Group 4","order":4}]}"""

        backupRestorer.restore(backup)

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
