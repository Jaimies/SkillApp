package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class StubBackupRepository(
    private val uploadResult: BackupRepository.Result<Unit> = backupRepositorySuccess,
    private val getContentsResult: BackupRepository.Result<BackupData> = getContentsSuccess,
    private val getBackupsResult: BackupRepository.Result<List<Backup>> = BackupRepository.Result.Success(listOf()),
    private val getLastBackupResult : BackupRepository.Result<Backup?> = BackupRepository.Result.Success(null),
) : BackupRepository {
    override suspend fun save(data: BackupData): BackupRepository.Result<Unit> {
        delay(2)
        return uploadResult
    }

    override suspend fun getContents(backup: Backup): BackupRepository.Result<BackupData> {
        delay(2)
        return getContentsResult
    }

    override suspend fun getBackups() = getBackupsResult
    override suspend fun getLastBackup() = getLastBackupResult

    // TODO: use something more reliable than a delay()
    override fun getLastBackupFlow() = flow {
        delay(20)
        emit(getLastBackupResult)
    }
}
