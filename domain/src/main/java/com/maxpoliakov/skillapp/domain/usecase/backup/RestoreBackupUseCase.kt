package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import kotlinx.coroutines.flow.StateFlow

interface RestoreBackupUseCase {
    val state: StateFlow<RestorationState>

    suspend fun restoreBackup(backup: Backup): Result

    sealed class Result {
        object Success : Result()
        object AlreadyInProgress : Result()
        class FetchFailure(val result: BackupRepository.Result.Failure) : Result()
        class RestorationFailure(val result: BackupRestorer.Result.Failure) : Result()
    }

    enum class RestorationState {
        Active, Inactive,
    }
}
