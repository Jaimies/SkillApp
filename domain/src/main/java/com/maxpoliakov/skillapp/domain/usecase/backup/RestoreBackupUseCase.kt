package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.result.BackupRestorationResult
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.flow.StateFlow

interface RestoreBackupUseCase {
    val state: StateFlow<RestorationState>

    suspend fun restoreBackup(backup: Backup): Result

    sealed class Result {
        object Success : Result()
        object AlreadyInProgress : Result()
        class FetchFailure(val result: BackupRepository.Result.Failure) : Result()
        class RestorationFailure(val result: BackupRestorationResult.Failure) : Result()
    }

    enum class RestorationState {
        Active, Inactive,
    }
}
