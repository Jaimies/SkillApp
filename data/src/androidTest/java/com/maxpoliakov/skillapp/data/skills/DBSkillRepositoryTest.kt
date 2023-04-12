package com.maxpoliakov.skillapp.data.skills

import com.maxpoliakov.skillapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.DBSkillGroupRepository
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.mapToDB
import com.maxpoliakov.skillapp.data.stats.DBGroupStatsRepository
import com.maxpoliakov.skillapp.data.stats.DBSkillStatsRepository
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.GroupStatsRepository
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.SkillStatsRepository
import kotlinx.coroutines.flow.first
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDate

class DBSkillRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var recordsDao: RecordsDao
    private lateinit var groupRepository: SkillGroupRepository
    private lateinit var skillStatsRepository: SkillStatsRepository
    private lateinit var groupStatsRepository: GroupStatsRepository

    @Before
    fun setup() {
        db = createTestDatabase()
        skillDao = db.skillDao()
        recordsDao = db.recordsDao()
        groupRepository = DBSkillGroupRepository(db.skillGroupDao())
        skillStatsRepository = DBSkillStatsRepository(db.statsDao())
        groupStatsRepository = DBGroupStatsRepository(db.skillGroupDao(), skillStatsRepository)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getCountAtDay() = runBlocking {
        val skills = listOf(
            createSkill("name", Duration.ofHours(2), 1),
            createSkill("other name", Duration.ofHours(20), 2),
        ).onEach { skill -> skillDao.insert(skill.mapToDB()) }

        val dateRange = LocalDate.now()..LocalDate.now()

        groupRepository.createGroup(createGroup(skills))

        skillStatsRepository.addRecord(createRecord(1, Duration.ofHours(2)))
        skillStatsRepository.addRecord(createRecord(1, Duration.ofHours(4)))
        skillStatsRepository.addRecord(createRecord(2, Duration.ofHours(3)))

        skillStatsRepository.getCount(1, dateRange).first() shouldBe Duration.ofHours(6).toMillis()
        groupStatsRepository.getCount(1, dateRange).first() shouldBe Duration.ofHours(9).toMillis()
    }

    private fun createSkill(name: String, totalTime: Duration, id: Int): Skill {
        return Skill(
            name,
            MeasurementUnit.Millis,
            totalTime.toMillis(),
            totalTime.toMillis() / 2,
            id = id,
        )
    }

    private fun createRecord(skillId: Int, time: Duration): Record {
        return Record("", skillId, time.toMillis(), MeasurementUnit.Millis)
    }

    private fun createGroup(skills: List<Skill>): SkillGroup {
        return SkillGroup(0, "new group", skills, MeasurementUnit.Millis, null, 0)
    }
}
