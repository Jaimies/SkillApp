package com.maxpoliakov.skilapp.data.stats

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxpoliakov.skilapp.data.await
import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.activities.ActivitiesDao
import com.maxpoliakov.skillapp.data.activities.DBActivity
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.data.stats.StatsDao
import com.maxpoliakov.skillapp.shared.util.daysSinceEpoch
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class StatsDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var statsDao: StatsDao
    private lateinit var activitiesDao: ActivitiesDao

    @Before
    fun beforeEach() = runBlocking {
        db = createTestDatabase()
        statsDao = db.statsDao()
        activitiesDao = db.activitiesDao()
        activitiesDao.insert(DBActivity())
    }

    @Test
    fun addRecord() = runBlocking {
        statsDao.addRecord(activityId, day, recordTime)
        statsDao.getStats(activityId).await() shouldBe listOf(
            DBStatistic(day, activityId, Duration.ofMinutes(100))
        )
    }

    @Test
    fun addRecord_recordAtGivenDayExists_sumsTime() = runBlocking {
        statsDao.addRecord(activityId, day, recordTime)
        statsDao.addRecord(activityId, day, recordTime)
        statsDao.getStats(activityId).await() shouldBe listOf(
            DBStatistic(day, activityId, Duration.ofMinutes(200))
        )
    }

    @Test
    fun addRecord_multipleActivitiesAtOneDay_areHandledIndependently() = runBlocking {
        activitiesDao.insert(DBActivity())
        statsDao.addRecord(activityId, day, recordTime)
        statsDao.addRecord(otherActivityId, day, recordTime)
        statsDao.getStats(activityId).await() shouldBe listOf(
            DBStatistic(day, activityId, Duration.ofMinutes(100))
        )
    }

    @Test
    fun getStats_selectsOnlyFromSpecifiedActivity() = runBlocking {
        statsDao.addRecord(activityId, day, recordTime)
        statsDao.getStats(otherActivityId).await() shouldBe listOf()
    }

    @Test
    fun getStats_selectsOnlyWithPositiveTime() = runBlocking {
        statsDao.addRecord(activityId, day, -recordTime)
        statsDao.getStats(activityId).await() shouldBe listOf()
    }

    @Test
    fun getStats_ignoresOlderThan13DaysAgo() = runBlocking {
        statsDao.addRecord(activityId, day - 14, recordTime)
        statsDao.getStats(activityId).await() shouldBe listOf()
    }

    @Test
    fun getStats_ignoresMoreRecentThanToday() = runBlocking {
        statsDao.addRecord(activityId, day + 1, recordTime)
        statsDao.getStats(activityId).await() shouldBe listOf()
    }

    @After
    fun afterEach() {
        db.close()
    }

    companion object {
        private val day = LocalDate.now().daysSinceEpoch
        private const val activityId = 1
        private const val otherActivityId = 2
        private const val recordTime = 100L
    }
}
