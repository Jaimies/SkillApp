package com.maxpoliakov.skillapp.domain.repository

class StubBackupCreator(
    private val result: BackupCreator.Result = BackupCreator.Result.Success(backupData),
) : BackupCreator {
    override suspend fun create() = result
}
