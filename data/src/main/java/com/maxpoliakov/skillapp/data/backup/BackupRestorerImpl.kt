package com.maxpoliakov.skillapp.data.backup

import androidx.room.withTransaction
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class BackupRestorerImpl @Inject constructor(
    private val db: AppDatabase,
) : BackupRestorer {
    override suspend fun restore(backupData: String) {
        val backup = Json.decodeFromString<BackupData>(backupData)

        db.withTransaction {
            db.skillDao().deleteAll()
            db.skillDao().insert(backup.skills)

            db.recordsDao().deleteAll()
            db.recordsDao().insert(backup.records)

            db.skillGroupDao().deleteAll()
            db.skillGroupDao().insert(backup.groups)

            db.statsDao().deleteAll()
            db.statsDao().insert(backup.stats)
        }
    }
}
