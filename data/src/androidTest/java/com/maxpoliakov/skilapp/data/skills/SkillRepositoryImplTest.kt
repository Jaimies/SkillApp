package com.maxpoliakov.skilapp.data.skills

import com.maxpoliakov.skilapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.SkillGroupRepositoryImpl
import com.maxpoliakov.skillapp.data.records.RecordsDao
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.mapToDB
import com.maxpoliakov.skillapp.data.stats.StatsRepositoryImpl
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.repository.SkillGroupRepository
import com.maxpoliakov.skillapp.domain.repository.StatsRepository
import com.maxpoliakov.skillapp.test.await
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Duration
import java.time.LocalDate

class SkillRepositoryImplTest {
    private lateinit var db: AppDatabase
    private lateinit var skillDao: SkillDao
    private lateinit var recordsDao: RecordsDao
    private lateinit var groupRepository: SkillGroupRepository
    private lateinit var statsRepository: StatsRepository

    @Before
    fun setup() {
        db = createTestDatabase()
        skillDao = db.skillDao()
        recordsDao = db.recordsDao()
        groupRepository = SkillGroupRepositoryImpl(db.skillGroupDao())
        statsRepository = StatsRepositoryImpl(db.statsDao(), groupRepository)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getGroupTimeToday() = runBlocking {
        val skills = listOf(
            Skill("name", Duration.ofHours(2), Duration.ofHours(1), id = 1),
            Skill("other name", Duration.ofHours(20), Duration.ofHours(10), id = 2),
        ).onEach { skill -> skillDao.insert(skill.mapToDB()) }

        groupRepository.createGroup(SkillGroup(1, "new group", skills, null, 0))

        statsRepository.addRecord(Record("", 1, Duration.ofHours(2)))
        statsRepository.addRecord(Record("", 2, Duration.ofHours(3)))

        statsRepository.getGroupTimeAtDate(1, LocalDate.now()).await() shouldBe Duration.ofHours(5)
    }
}
