package com.theskillapp.skillapp.domain.usecase.backup

import com.theskillapp.skillapp.domain.model.Backup
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.repository.BackupRestorer
import kotlinx.coroutines.flow.StateFlow

interface RestoreBackupUseCase {
    val state: StateFlow<RestorationState>

    suspend fun restoreBackup(backup: Backup): Result

    sealed class Result {
        object Success : Result()
        object AlreadyInProgress : Result()
        data class FetchFailure(val result: BackupRepository.Result.Failure) : Result()
        data class RestorationFailure(val result: BackupRestorer.Result.Failure) : Result()
    }

    enum class RestorationState {
        Active, Inactive,
    }
}
