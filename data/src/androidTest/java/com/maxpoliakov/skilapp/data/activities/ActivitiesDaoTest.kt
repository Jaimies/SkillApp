package com.maxpoliakov.skilapp.data.activities

import com.maxpoliakov.skilapp.data.await
import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.activities.ActivitiesDao
import com.maxpoliakov.skillapp.data.activities.DBActivity
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.shared.util.setClock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId

class ActivitiesDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var activitiesDao: ActivitiesDao
    private lateinit var recordsDao: RecordsDao

    @Before
    fun setup() {
        db = createTestDatabase()
        activitiesDao = db.activitiesDao()
        recordsDao = db.recordsDao()
        OffsetDateTime.now().withHour(22).toInstant()
        setClock(Clock.fixed(OffsetDateTime.now().withHour(22).toInstant(), ZoneId.systemDefault()))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getActivities_sortsProperly() = runBlocking {
        activitiesDao.insert(DBActivity(totalTime = Duration.ofHours(2)))
        activitiesDao.insert(DBActivity(totalTime = Duration.ofHours(3)))
        activitiesDao.getActivities().await() shouldBe listOf(
            DBActivity(id = 2, totalTime = Duration.ofHours(3)),
            DBActivity(id = 1, totalTime = Duration.ofHours(2))
        )
    }

    @Test
    fun getActivity_calculatesLastWeekTime() = runBlocking {
        activitiesDao.insert(DBActivity())

        val recordDates = listOf(
            OffsetDateTime.now().minusDays(6).minusMinutes(1),
            OffsetDateTime.now().plusDays(1).withHour(0),
            OffsetDateTime.now().withHour(23)
        )

        recordDates.forEach { timestamp ->
            recordsDao.insert(
                DBRecord(activityId = 1, time = Duration.ofHours(3), timestamp = timestamp)
            )
        }

        activitiesDao.getActivity(1).await() shouldBe DBActivity(
            id = 1,
            lastWeekTime = Duration.ofHours(3)
        )
    }

    @Test
    fun increaseTime() = runBlocking {
        activitiesDao.insert(DBActivity(totalTime = Duration.ZERO))
        activitiesDao.increaseTime(1, 100)
        activitiesDao.getActivity(1).await().totalTime shouldBe Duration.ofMinutes(100)
    }
}
