package com.maxpoliakov.skillapp.data.backup

import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.DBGroup
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BackupUtilImpl @Inject constructor(
    private val db: AppDatabase,
) : BackupUtil {
    override suspend fun getDatabaseBackup() = coroutineScope {
        val skillsAsync = async { db.skillDao().getAllSkills() }
        val recordsAsync = async { db.recordsDao().getAllRecords() }
        val statsAsync = async { db.statsDao().getAllStats() }
        val groupsAsync = async { db.skillGroupDao().getAllGroups() }

        val backupData = BackupData(
            skills = skillsAsync.await(),
            records = recordsAsync.await(),
            stats = statsAsync.await(),
            groups = groupsAsync.await()
        )

        Json.encodeToString(backupData)
    }
}

@Serializable
data class BackupData(
    val skills: List<DBSkill>,
    val records: List<DBRecord>,
    val stats: List<DBStatistic>,
    val groups: List<DBGroup>,
)
