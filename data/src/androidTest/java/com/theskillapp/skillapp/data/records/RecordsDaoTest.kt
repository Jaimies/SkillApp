package com.theskillapp.skillapp.data.records

import androidx.paging.PagingSource
import com.theskillapp.skillapp.data.createTestDatabase
import com.theskillapp.skillapp.data.db.AppDatabase
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.skill.SkillDao
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecordsDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var recordsDao: RecordsDao
    private lateinit var skillDao: SkillDao

    @Before
    fun beforeEach() {
        db = createTestDatabase()
        recordsDao = db.recordsDao()
        skillDao = db.skillDao()
    }

    @After
    fun afterEach() {
        db.close()
    }

    @Test
    fun getRecords_getsNameForRecordFromItsSkill() = runBlocking {
        skillDao.insert(skill)
        recordsDao.insert(record)
        val loadResult = recordsDao.getRecords().load(PagingSource.LoadParams.Refresh(0, 100, false))

        loadResult should beInstanceOf<PagingSource.LoadResult.Page<*, *>>()
        val data = (loadResult as PagingSource.LoadResult.Page).data
        data[0].recordName shouldBe "Skill name"
    }

    companion object {
        val skill = DBSkill(name = "Skill name")
        val record = DBRecord(time = 1296, skillId = 1)
    }
}
