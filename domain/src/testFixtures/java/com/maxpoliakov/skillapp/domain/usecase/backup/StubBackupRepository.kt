package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

class StubBackupRepository(
    private val uploadResult: BackupRepository.Result<Unit> = backupRepositorySuccess,
    private val getContentsResult: BackupRepository.Result<BackupData> = getContentsSuccess,
) : BackupRepository {
    override suspend fun save(data: BackupData): BackupRepository.Result<Unit> {
        delay(2)
        return uploadResult
    }

    override suspend fun getContents(backup: Backup): BackupRepository.Result<BackupData> {
        delay(2)
        return getContentsResult
    }

    override suspend fun getBackups() = BackupRepository.Result.Success(listOf<Backup>())
    override suspend fun getLastBackup() = BackupRepository.Result.Success(null)
    override fun getLastBackupFlow() = flowOf<BackupRepository.Result<Backup?>>()
}
