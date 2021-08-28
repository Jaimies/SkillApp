package com.maxpoliakov.skillapp.data.backup

import androidx.room.withTransaction
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.group.DBGroup
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
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

    override suspend fun restoreBackup(backupData: String) {
        val backup = Json.decodeFromString<BackupData>(backupData)

        db.withTransaction {
            db.skillGroupDao().deleteAll()
            db.skillGroupDao().insert(backup.groups)

            db.recordsDao().deleteAll()
            db.recordsDao().insert(backup.records)

            db.skillDao().deleteAll()
            db.skillDao().insert(backup.skills)

            db.statsDao().deleteAll()
            db.statsDao().insert(backup.stats)
        }
    }
}

@Serializable
data class BackupData(
    val skills: List<DBSkill>,
    val records: List<DBRecord>,
    val stats: List<DBStatistic>,
    val groups: List<DBGroup>,
)
