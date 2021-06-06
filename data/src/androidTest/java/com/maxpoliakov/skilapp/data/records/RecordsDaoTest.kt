package com.maxpoliakov.skilapp.data.records

import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skilapp.data.getValue
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.records.RecordsDao
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Duration

class RecordsDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var recordsDao: RecordsDao
    private lateinit var skillDao: SkillDao

    @Before
    fun beforeEach() {
        db = createTestDatabase()
        recordsDao = db.recordsDao()
        skillDao = db.skillDao()
    }

    @After
    fun afterEach() {
        db.close()
    }

    @Test
    fun getRecords() = runBlocking {
        skillDao.insert(DBSkill(name = "Skill name"))
        recordsDao.insert(DBRecord(time = Duration.ofMinutes(50), skillId = 1))
        val record = recordsDao.getRecords().getValue()[0]
        record.recordName shouldBe "Skill name"
    }
}
