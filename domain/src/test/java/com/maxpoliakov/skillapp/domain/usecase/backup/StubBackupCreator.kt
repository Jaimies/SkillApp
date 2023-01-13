package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.repository.BackupCreator

class StubBackupCreator(
    private val result: BackupCreator.Result = BackupCreator.Result.Success(backupData),
) : BackupCreator {
    override suspend fun create() = result
}
