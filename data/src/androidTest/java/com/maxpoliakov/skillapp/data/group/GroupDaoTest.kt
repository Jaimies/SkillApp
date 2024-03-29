package com.maxpoliakov.skillapp.data.group

import com.maxpoliakov.skillapp.data.createTestDatabase
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.skill.SkillDao
import com.maxpoliakov.skillapp.data.skill.mapToDomain
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GroupDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var groupDao: GroupDao
    private lateinit var skillDao: SkillDao

    @Before
    fun setup() {
        db = createTestDatabase()
        groupDao = db.skillGroupDao()
        skillDao = db.skillDao()
    }

    @Test
    fun createGroup() = runBlocking {
        val skills = listOf(
            DBSkill(id = 1, name = "Skill"),
            DBSkill(id = 2, name = "Other skill"),
        )

        skillDao.insert(skills)

        val group = SkillGroup(1, "Group name", skills.map(DBSkill::mapToDomain), MeasurementUnit.Millis, null, -1)
        groupDao.createGroup(group)
        val groupFromDB = groupDao.getGroupById(1)!!

        groupFromDB.mapToDomain() shouldBe group.copy(
            skills = group.skills.map { it.copy(groupId = 1) }
        )
    }
}
