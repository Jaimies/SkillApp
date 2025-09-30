package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.BackupData

class StubBackupRestorer(
    private val result: BackupRestorer.Result = BackupRestorer.Result.Success,
) : BackupRestorer {
    var restorationCount = 0
        private set

    override suspend fun restore(data: BackupData): BackupRestorer.Result {
        restorationCount++
        return result
    }
}
