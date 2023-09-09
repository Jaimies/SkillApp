package com.maxpoliakov.skillapp.data.timers

import com.maxpoliakov.skillapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.timer.DBTimer
import com.maxpoliakov.skillapp.data.timer.TimerDao
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TimerDaoTest {
    lateinit var db: AppDatabase
    lateinit var skillDao: SkillDao
    lateinit var timerDao: TimerDao

    @Before
    fun setup() {
        db = createTestDatabase()
        skillDao = db.skillDao()
        timerDao = db.timerDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insert_does_nothing_if_timer_for_that_skill_already_exists() = runBlocking {
        skillDao.insert(DBSkill(id = 1))
        timerDao.insert(DBTimer(skillId = 1, startTime = date))
        timerDao.insert(DBTimer(skillId = 1, startTime = date.plusHours(1)))

        timerDao.getAll().first() shouldBe listOf(DBTimer(skillId = 1, startTime = date))
    }

    companion object {
        private val date = ZonedDateTime.ofLocal(
            LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC), ZoneId.systemDefault(),
            ZoneOffset.UTC,
        )
    }
}
