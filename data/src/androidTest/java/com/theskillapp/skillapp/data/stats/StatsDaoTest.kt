package com.theskillapp.skillapp.data.stats

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.theskillapp.skillapp.data.createTestDatabase
import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.skill.SkillDao
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
    fun getStatsGroupedByDate_selectsOnlyStatsForGivenSkillId() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())
        statsDao.addRecord(yetAnotherSkillId, date, recordTime.toMillis())

        statsDao.getStatsGroupedByDate(listOf(skillId), date, date).first() shouldBe listOf(
            DBStatistic(date, 0, recordTime.toMillis()),
        )
    }

    @Test
    fun getStatsGroupedByDate_selectsOnlyStatsInGivenRange() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(2), recordTime.toMillis())
        statsDao.addRecord(skillId, date.minusDays(1), recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date.plusDays(1), recordTime.toMillis())

        statsDao.getStatsGroupedByDate(listOf(skillId), date.minusDays(1), date).first() shouldBe listOf(
            DBStatistic(date.minusDays(1), 0, recordTime.toMillis()),
            DBStatistic(date, 0, recordTime.toMillis()),
        )
    }

    @Test
    fun getStatsGroupedByDate_groupsStatsByDate() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(1), recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date.minusDays(1), recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())

        statsDao.getStatsGroupedByDate(listOf(skillId, otherSkillId), date.minusDays(1), date).first() shouldBe listOf(
            DBStatistic(date.minusDays(1), 0, recordTime.toMillis() * 2),
            DBStatistic(date, 0, recordTime.toMillis() * 2),
        )
    }

    @Test
    fun addRecord_addsTimeRatherThanAddingNewRecord_ifRecordForThatDateAlreadyExists() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())

        statsDao.getAllStats() shouldBe listOf(
            DBStatistic(date, skillId, recordTime.multipliedBy(2).toMillis()),
        )
    }

    @Test
    fun getStatsGroupedByDate_selectsOnlyWithPositiveTime() = runBlocking {
        statsDao.addRecord(skillId, date, recordTime.negated().toMillis())
        statsDao.getStatsGroupedByDate(listOf(skillId), date, date).first() shouldBe listOf()
    }

    @Test
    fun getCountInDateRange() = runBlocking {
        statsDao.addRecord(skillId, date.minusDays(2), recordTime.toMillis())
        statsDao.addRecord(skillId, date.minusDays(1), recordTime.toMillis())
        statsDao.addRecord(skillId, date, recordTime.toMillis())
        statsDao.addRecord(otherSkillId, date, recordTime.toMillis())
        statsDao.addRecord(yetAnotherSkillId, date, recordTime.toMillis())
        statsDao.addRecord(skillId, date.plusDays(1), recordTime.toMillis())

        statsDao.getCountInDateRange(
            listOf(skillId, otherSkillId),
            date.minusDays(1),
            date,
        ).first() shouldBe recordTime.toMillis() * 3
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
