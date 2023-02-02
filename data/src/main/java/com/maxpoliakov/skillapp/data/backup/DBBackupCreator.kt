package com.maxpoliakov.skillapp.data.backup

import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.log
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DBBackupCreator @Inject constructor(
    private val db: AppDatabase,
    private val json: Json,
) : BackupCreator {
    override suspend fun create(): BackupCreator.Result {
        try {
            val data = createBackup()
            return BackupCreator.Result.Success(data)
        } catch (e: Exception) {
            e.log()
            return BackupCreator.Result.Failure(e)
        }
    }

    private suspend fun createBackup(): BackupData = coroutineScope {
        val skillsAsync = async { db.skillDao().getAllSkills() }
        val recordsAsync = async { db.recordsDao().getAllRecords() }
        val statsAsync = async { db.statsDao().getAllStats() }
        val groupsAsync = async { db.skillGroupDao().getAllGroups() }

        val backupData = DBBackupData(
            skills = skillsAsync.await(),
            records = recordsAsync.await(),
            stats = statsAsync.await(),
            groups = groupsAsync.await()
        )

        BackupData(
            json.encodeToString(backupData),
        )
    }
}
