package com.maxpoliakov.skilapp.data.backup

import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.backup.BackupUtilImpl
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.DBGroup
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.time.Duration

class BackupUtilImplTest {
    private lateinit var db: AppDatabase
    private lateinit var backupUtil: BackupUtil

    @Before
    fun setup() {
        db = createTestDatabase()
        backupUtil = BackupUtilImpl(db)
    }

    @Test
    fun backupAndRestore() = runBlocking {
        val skills = List(5) { index ->
            DBSkill(
                id = index + 1,
                name = "Skill $index",
                totalTime = Duration.ofHours(index.toLong()),
                initialTime = Duration.ofMinutes(index.toLong()),
            )
        }

        val records = List(5) { index ->
            DBRecord(
                id = index + 1,
                time = Duration.ofHours(index.toLong()),
                skillId = index + 1,
            )
        }

        val statistics = List(5) { index ->
            DBStatistic(
                skillId = index + 1,
                time = Duration.ofHours(index.toLong()),
                date = getCurrentDate(),
            )
        }

        val groups = List(5) { index ->
            DBGroup(id = index + 1, name = "Group $index", order = index)
        }

        db.skillDao().insert(skills)
        db.recordsDao().insert(records)
        db.statsDao().insert(statistics)
        db.skillGroupDao().insert(groups)

        val backup = backupUtil.getDatabaseBackup()

        db.skillDao().deleteAll()
        db.recordsDao().deleteAll()
        db.skillGroupDao().deleteAll()
        db.statsDao().deleteAll()

        backupUtil.restoreBackup(backup)

        db.skillDao().getAllSkills() shouldBe skills
        db.recordsDao().getAllRecords() shouldBe records
        db.statsDao().getAllStats() shouldBe statistics
        db.skillGroupDao().getAllGroups() shouldBe groups
    }
}
