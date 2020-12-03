package com.maxpoliakov.skilapp.data.stats

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxpoliakov.skillapp.test.await
import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.DBSkill
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
    private lateinit var skillDao: SkillDao

    @Before
    fun beforeEach() = runBlocking {
        db = createTestDatabase()
        statsDao = db.statsDao()
        skillDao = db.statisticDao()
        skillDao.insert(DBSkill())
    }

    @Test
    fun addRecord() = runBlocking {
        statsDao.addRecord(skillId, day, recordTime)
        statsDao.getStats(skillId).await() shouldBe listOf(
            DBStatistic(day, skillId, Duration.ofMinutes(100))
        )
    }

    @Test
    fun addRecord_recordAtGivenDayExists_sumsTime() = runBlocking {
        statsDao.addRecord(skillId, day, recordTime)
        statsDao.addRecord(skillId, day, recordTime)
        statsDao.getStats(skillId).await() shouldBe listOf(
            DBStatistic(day, skillId, Duration.ofMinutes(200))
        )
    }

    @Test
    fun addRecord_multipleSkillsAtOneDay_areHandledIndependently() = runBlocking {
        skillDao.insert(DBSkill())
        statsDao.addRecord(skillId, day, recordTime)
        statsDao.addRecord(otherSkillId, day, recordTime)
        statsDao.getStats(skillId).await() shouldBe listOf(
            DBStatistic(day, skillId, Duration.ofMinutes(100))
        )
    }

    @Test
    fun getStats_selectsOnlyFromSpecifiedSkill() = runBlocking {
        statsDao.addRecord(skillId, day, recordTime)
        statsDao.getStats(otherSkillId).await() shouldBe listOf()
    }

    @Test
    fun getStats_selectsOnlyWithPositiveTime() = runBlocking {
        statsDao.addRecord(skillId, day, -recordTime)
        statsDao.getStats(skillId).await() shouldBe listOf()
    }

    @Test
    fun getStats_ignoresOlderThan13DaysAgo() = runBlocking {
        statsDao.addRecord(skillId, day - 14, recordTime)
        statsDao.getStats(skillId).await() shouldBe listOf()
    }

    @Test
    fun getStats_ignoresMoreRecentThanToday() = runBlocking {
        statsDao.addRecord(skillId, day + 1, recordTime)
        statsDao.getStats(skillId).await() shouldBe listOf()
    }

    @Test
    fun getStats_getsTotal() = runBlocking {
        skillDao.insert(DBSkill())
        statsDao.addRecord(skillId, day, recordTime)
        statsDao.addRecord(otherSkillId, day, recordTime)
        statsDao.getStats(-1).await() shouldBe listOf(
            DBStatistic(day, -1, Duration.ofMinutes(200))
        )
    }

    @After
    fun afterEach() {
        db.close()
    }

    companion object {
        private val day = LocalDate.now().daysSinceEpoch
        private const val skillId = 1
        private const val otherSkillId = 2
        private const val recordTime = 100L
    }
}
