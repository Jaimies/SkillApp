package com.maxpoliakov.skilapp.data.stats

import com.maxpoliakov.skillapp.data.activities.ActivitiesDao
import com.maxpoliakov.skillapp.data.activities.DBActivity
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
    private lateinit var activitiesDao: ActivitiesDao

    @Before
    fun beforeEach() {
        db = createTestDatabase()
        recordsDao = db.recordsDao()
        activitiesDao = db.activitiesDao()
    }

    @After
    fun afterEach() {
        db.close()
    }

    @Test
    fun getRecords() = runBlocking {
        activitiesDao.insert(DBActivity(name = "Activity name"))
        recordsDao.insert(DBRecord(time = Duration.ofMinutes(50), activityId = 1))
        val record = recordsDao.getRecords().getValue()[0]
        record.activityName shouldBe "Activity name"
    }
}
