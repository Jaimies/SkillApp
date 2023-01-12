package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.result.BackupCreationResult
import com.maxpoliakov.skillapp.domain.repository.BackupRepository

interface PerformBackupUseCase {
    suspend fun performBackup(): Result

    sealed class Result {
        object Success : Result()
        class CreationFailure(val creationResult: BackupCreationResult.Failure) : Result()
        class UploadFailure(val uploadResult: BackupRepository.Result.Failure) : Result()
    }
}
