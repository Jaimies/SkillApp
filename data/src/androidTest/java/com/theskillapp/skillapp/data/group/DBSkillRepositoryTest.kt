package com.theskillapp.skillapp.data.group

import com.theskillapp.skillapp.data.createTestDatabase
import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.records.RecordsDao
import com.theskillapp.skillapp.data.skill.SkillDao
import com.theskillapp.skillapp.data.skill.DBSkillRepository
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.domain.repository.SkillGroupRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class DBSkillRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var recordsDao: RecordsDao
    private lateinit var groupRepository: SkillGroupRepository
    private lateinit var skillRepository: DBSkillRepository

    @Before
    fun setup() {
        db = createTestDatabase()
        skillDao = db.skillDao()
        recordsDao = db.recordsDao()
        groupRepository = DBSkillGroupRepository(db.skillGroupDao())
        skillRepository = DBSkillRepository(db.skillDao())
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun skillGroupRepository_updateGoal() = runBlocking {
        groupRepository.createGroup(SkillGroup(1, "new group", listOf(), MeasurementUnit.Millis, null, 0))
        groupRepository.updateGoal(1, goal)
        groupRepository.getSkillGroupById(1)?.goal shouldBe goal
    }

    @Test
    fun skillRepository_updateGoal() = runBlocking {
        skillRepository.addSkill(Skill("", MeasurementUnit.Millis, 0, 0, goal = null))
        skillRepository.updateGoal(1, goal)
        skillRepository.getSkillById(1)?.goal shouldBe goal
    }

    companion object {
        private val goal = Goal(4_000_000, Goal.Type.Weekly)
    }
}
