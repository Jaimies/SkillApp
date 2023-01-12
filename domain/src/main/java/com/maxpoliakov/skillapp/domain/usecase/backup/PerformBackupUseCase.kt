package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository

interface PerformBackupUseCase {
    suspend fun performBackup(): Result

    sealed class Result {
        object Success : Result()
        class CreationFailure(val creationResult: BackupCreator.Result.Failure) : Result()
        class UploadFailure(val uploadResult: BackupRepository.Result.Failure) : Result()
    }
}
