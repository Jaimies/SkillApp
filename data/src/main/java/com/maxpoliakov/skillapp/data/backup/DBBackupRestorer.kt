package com.maxpoliakov.skillapp.data.backup

import androidx.room.withTransaction
import com.maxpoliakov.skillapp.data.db.AppDatabase
import com.maxpoliakov.skillapp.data.log
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class DBBackupRestorer @Inject constructor(
    private val db: AppDatabase,
) : BackupRestorer {
    override suspend fun restore(data: BackupData): BackupRestorer.Result {
        try {
            doRestore(data)
            return BackupRestorer.Result.Success
        } catch (e: Throwable) {
            e.log()
            return BackupRestorer.Result.Failure(e)
        }
    }

    // todo consider better name
    private suspend fun doRestore(data: BackupData) {
        val backup = Json.decodeFromString<DBBackupData>(data.contents)

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
