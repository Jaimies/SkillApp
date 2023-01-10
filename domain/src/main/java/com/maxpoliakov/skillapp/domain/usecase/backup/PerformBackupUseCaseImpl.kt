package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.result.BackupCreationResult
import com.maxpoliakov.skillapp.domain.repository.BackupRepository.Result as BackupUploadResult
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase.Result
import javax.inject.Inject

class PerformBackupUseCaseImpl @Inject constructor(
    private val backupRepository: BackupRepository,
    private val backupCreator: BackupCreator,
) : PerformBackupUseCase {

    override suspend fun performBackup(): Result {
        val result = backupCreator.create()

        return when (result) {
            is BackupCreationResult.Success -> uploadBackup(result.data)
            is BackupCreationResult.Failure -> Result.CreationFailure(result)
        }
    }

    private suspend fun uploadBackup(backup: BackupData): Result {
        val result = backupRepository.upload(backup)

        return when (result) {
            is BackupUploadResult.Success -> Result.Success
            is BackupUploadResult.Failure -> Result.UploadFailure(result)
        }
    }
}
