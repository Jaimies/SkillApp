package com.maxpoliakov.skilapp.data.stats

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.data.stats.StatsDao
import com.maxpoliakov.skillapp.test.await
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
    fun beforeEach() = runBlocking<Unit> {
        db = createTestDatabase()
        statsDao = db.statsDao()
        skillDao = db.skillDao()
        skillDao.insert(DBSkill())
    }

    @Test
    fun addRecord() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf(
            DBStatistic(date, skillId, Duration.ofMinutes(100).toMillis())
        )
    }

    @Test
    fun addRecord_recordAtGivenDayExists_sumsTime() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf(
            DBStatistic(date, skillId, Duration.ofMinutes(200).toMillis())
        )
    }

    @Test
    fun addRecord_multipleSkillsAtOneDay_areHandledIndependently() = runBlocking {
        skillDao.insert(DBSkill())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf(
            DBStatistic(date, skillId, Duration.ofMinutes(100).toMillis())
        )
    }

    @Test
    fun getStats_selectsOnlyFromSpecifiedSkill() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.getStats(otherSkillId, 7).await() shouldBe listOf()
    }

    @Test
    fun getStats_selectsOnlyWithPositiveTime() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.negated().toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf()
    }

    @Test
    fun getStats_includes6DaysAgo() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(6), recordTime.toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf(
            DBStatistic(date.minusDays(6), skillId, recordTime.toMillis())
        )
    }

    @Test
    fun getStats_ignoresOlderThan55DaysAgo() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(56), recordTime.toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf()
    }

    @Test
    fun getStats_ignoresMoreRecentThanToday() = runBlocking {
        statsDao.addRecord(skillId, date.plusDays(1), recordTime.toMillis())
        statsDao.getStats(skillId, 7).await() shouldBe listOf()
    }

    @Test
    fun getTimeAtDate() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(1), Duration.ofMinutes(150).toMillis())
        statsDao.addRecord(skillId, date, Duration.ofMinutes(200).toMillis())
        statsDao.getCountAtDate(date) shouldBe Duration.ofMinutes(200)
        statsDao.getCountAtDate(date.minusDays(1)) shouldBe Duration.ofMinutes(150)
    }

    @Test
    fun getStats_getsTotal() = runBlocking {
        skillDao.insert(DBSkill())
        statsDao.addRecord(skillId, date.minusDays(56), recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())
        statsDao.getStats(-1, 7).await() shouldBe listOf(
            DBStatistic(date, -1, Duration.ofMinutes(200).toMillis())
        )
    }

    @After
    fun afterEach() {
        db.close()
    }

    companion object {
        private val date = LocalDate.now()
        private val recordTime = Duration.ofMinutes(100)
        private const val skillId = 1
        private const val otherSkillId = 2
    }
}
