package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import javax.inject.Inject

class CreateBackupUseCaseImpl @Inject constructor(
    private val backupRepository: BackupRepository,
    private val backupCreator: BackupCreator,
) : CreateBackupUseCase {
    override suspend fun createBackup(): BackupUploadResult {
        return backupRepository.upload(backupCreator.create())
    }
}
