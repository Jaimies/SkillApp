package com.maxpoliakov.skilapp.data.group

import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.SkillGroupRepositoryImpl
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.SkillRepositoryImpl
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Duration

class SkillRepositoryImplTest {
    private lateinit var db: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var recordsDao: RecordsDao
    private lateinit var groupRepository: SkillGroupRepository
    private lateinit var skillRepository: SkillRepositoryImpl

    @Before
    fun setup() {
        db = createTestDatabase()
        skillDao = db.skillDao()
        recordsDao = db.recordsDao()
        groupRepository = SkillGroupRepositoryImpl(db.skillGroupDao())
        skillRepository = SkillRepositoryImpl(db.skillDao())
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
