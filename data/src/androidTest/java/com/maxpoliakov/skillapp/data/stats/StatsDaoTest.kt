package com.maxpoliakov.skillapp.data.stats

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.maxpoliakov.skillapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.test.await
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
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
        skillDao.insert(DBSkill(id = skillId))
        skillDao.insert(DBSkill(id = otherSkillId))
        skillDao.insert(DBSkill(id = yetAnotherSkillId))
    }

    @Test
    fun getStats_selectsOnlyStatsForGivenSkillId() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())
        statsDao.addRecord(yetAnotherSkillId, date, recordTime.toMillis())

        statsDao.getStats(skillId, date, date).await() shouldBe listOf(
            DBStatistic(date, skillId, recordTime.toMillis()),
        )
    }

    @Test
    fun getStats_selectsOnlyStatsInGivenRange() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(2), recordTime.toMillis())
        statsDao.addRecord(skillId, date.minusDays(1), recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date.plusDays(1), recordTime.toMillis())

        statsDao.getStats(skillId, date.minusDays(1), date).await() shouldBe listOf(
            DBStatistic(date.minusDays(1), skillId, recordTime.toMillis()),
            DBStatistic(date, skillId, recordTime.toMillis()),
        )
    }

    @Test
    fun getStats_sumsTime() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())

        statsDao.getStats(skillId, date, date).await() shouldBe listOf(
            DBStatistic(date, skillId, recordTime.multipliedBy(2).toMillis()),
        )
    }

    @Test
    fun getStats_selectsOnlyWithPositiveTime() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.negated().toMillis())
        statsDao.getStats(skillId, date, date).await() shouldBe listOf()
    }

    @Test
    fun getStats_getsStatsForAllSkillsWhenNegativeOnePassedAsId() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())

        statsDao.getStats(-1, date, date).await() shouldBe listOf(
            DBStatistic(date, -1, recordTime.multipliedBy(2).toMillis())
        )
    }

    @Test
    fun getCountInDateRange() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(2), recordTime.toMillis())
        statsDao.addRecord(skillId, date.minusDays(1), recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date.plusDays(1), recordTime.toMillis())

        statsDao.getCountInDateRange(skillId, date.minusDays(1), date).first() shouldBe recordTime.toMillis() * 2
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
        private const val yetAnotherSkillId = 3
    }
}
