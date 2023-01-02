package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.result.BackupCreationResult
import com.maxpoliakov.skillapp.domain.model.result.BackupResult
import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import javax.inject.Inject

class CreateBackupUseCaseImpl @Inject constructor(
    private val backupRepository: BackupRepository,
    private val backupCreator: BackupCreator,
) : CreateBackupUseCase {

    override suspend fun createBackup(): BackupResult {
        val creationResult = backupCreator.create()

        return when (creationResult) {
            is BackupCreationResult.Success -> uploadBackup(creationResult.data)
            is BackupCreationResult.Failure -> BackupResult.CreationFailure(creationResult)
        }
    }

    private suspend fun uploadBackup(backup: BackupData): BackupResult {
        val uploadResult = backupRepository.upload(backup)

        return when (uploadResult) {
            is BackupUploadResult.Success -> BackupResult.Success
            is BackupUploadResult.Failure -> BackupResult.UploadFailure(uploadResult)
        }
    }
}
