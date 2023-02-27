package com.maxpoliakov.skillapp.data.skills

import com.maxpoliakov.skillapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.mapToDB
import com.maxpoliakov.skillapp.data.skill.mapToDomain
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.util.setClock
import com.maxpoliakov.skillapp.test.await
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Duration
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
        skillDao.insert(DBSkill(totalTime = Duration.ofHours(1).toMillis()))
        skillDao.insert(DBSkill(totalTime = Duration.ofHours(2).toMillis()))

        skillDao.getSkills().await() shouldBe listOf(
            DBSkill(id = 2, totalTime = Duration.ofHours(2).toMillis()),
            DBSkill(id = 1, totalTime = Duration.ofHours(1).toMillis())
        )
    }

    @Test
    fun getSkill_skillDoesNotExist_returnsNull() = runBlocking {
        skillDao.getSkill(1) shouldBe null
    }

    @Test
    fun increaseTime() = runBlocking {
        skillDao.insert(DBSkill(totalTime = Duration.ofMinutes(10).toMillis()))
        skillDao.increaseCount(1, Duration.ofMinutes(20).toMillis())
        skillDao.getSkill(1)!!.totalTime shouldBe Duration.ofMinutes(30).toMillis()
    }

    @Test
    fun updateGoal() = runBlocking {
        val goal = Goal(Duration.ofHours(5).toMillis(), Goal.Type.Daily)
        val skill = Skill("", MeasurementUnit.Millis, 0, 0, id = 1)
        val dbSkill = skill.mapToDB()
        skillDao.insert(dbSkill)
        skillDao.updateGoal(skill.id, goal.count, goal.type)
        skillDao.getSkill(1)!!.mapToDomain() shouldBe skill.copy(goal = goal)
        skillDao.updateGoal(skill.id, 0, Goal.Type.Daily)
        skillDao.getSkill(1)!!.mapToDomain() shouldBe skill
    }
}
