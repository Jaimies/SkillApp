package com.theskillapp.skillapp.data.group

import com.theskillapp.skillapp.data.createTestDatabase
import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.skill.SkillDao
import com.theskillapp.skillapp.data.skill.mapToDomain
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.SkillGroup
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
