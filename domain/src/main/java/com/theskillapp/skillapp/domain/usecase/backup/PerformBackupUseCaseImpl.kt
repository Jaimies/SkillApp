package com.theskillapp.skillapp.domain.usecase.backup

import com.theskillapp.skillapp.domain.model.BackupData
import com.theskillapp.skillapp.domain.repository.BackupCreator
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase.Result
import javax.inject.Inject

class PerformBackupUseCaseImpl @Inject constructor(
    private val backupRepository: BackupRepository,
    private val backupCreator: BackupCreator,
) : PerformBackupUseCase {

    override suspend fun performBackup(): Result {
        val result = backupCreator.create()

        return when (result) {
            is BackupCreator.Result.Success -> uploadBackup(result.data)
            is BackupCreator.Result.Failure -> Result.CreationFailure(result)
        }
    }

    private suspend fun uploadBackup(backup: BackupData): Result {
        val result = backupRepository.save(backup)

        return when (result) {
            is BackupRepository.Result.Success -> Result.Success
            is BackupRepository.Result.Failure -> Result.UploadFailure(result)
        }
    }
}
