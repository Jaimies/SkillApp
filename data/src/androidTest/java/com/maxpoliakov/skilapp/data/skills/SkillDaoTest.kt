package com.maxpoliakov.skilapp.data.skills

import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.await
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId

class SkillDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var recordsDao: RecordsDao

    @Before
    fun setup() {
        db = createTestDatabase()
        skillDao = db.skillDao()
        recordsDao = db.recordsDao()
        OffsetDateTime.now().withHour(22).toInstant()
        setClock(Clock.fixed(OffsetDateTime.now().withHour(22).toInstant(), ZoneId.systemDefault()))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getSkills_sortsProperly() = runBlocking {
        skillDao.insert(DBSkill(totalTime = Duration.ofHours(2)))
        skillDao.insert(DBSkill(totalTime = Duration.ofHours(3)))
        skillDao.getSkills().await() shouldBe listOf(
            DBSkill(id = 2, totalTime = Duration.ofHours(3)),
            DBSkill(id = 1, totalTime = Duration.ofHours(2))
        )
    }

    @Test
    fun getSkill_calculatesLastWeekTime() = runBlocking {
        skillDao.insert(DBSkill())

        val recordDates = listOf(
            LocalDate.now().minusDays(7),
            LocalDate.now().minusDays(6),
            LocalDate.now().plusDays(1),
            LocalDate.now()
        )

        recordDates.forEach { timestamp ->
            recordsDao.insert(
                DBRecord(skillId = 1, time = Duration.ofHours(3), date = timestamp)
            )
        }

        skillDao.getSkill(1).await() shouldBe DBSkill(
            id = 1,
            lastWeekTime = Duration.ofHours(6)
        )
    }

    @Test
    fun getSkill_skillDoesNotExist_returnsNull() = runBlocking {
        skillDao.getSkill(1).await() shouldBe null
    }

    @Test
    fun increaseTime() = runBlocking {
        skillDao.insert(DBSkill(totalTime = Duration.ofMinutes(10)))
        skillDao.increaseTime(1, Duration.ofMinutes(20))
        skillDao.getSkill(1).await()!!.totalTime shouldBe Duration.ofMinutes(30)
    }
}
