package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.Result
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestoreBackupUseCaseImpl @Inject constructor(
    private val backupRepository: BackupRepository,
    private val backupRestorer: BackupRestorer,
) : RestoreBackupUseCase {
    private val _state = MutableStateFlow(RestorationState.Inactive)
    override val state: StateFlow<RestorationState> get() = _state

    override suspend fun restoreBackup(backup: Backup): Result {
        if (state.value == RestorationState.Active) {
            return Result.AlreadyInProgress
        }

        _state.emit(RestorationState.Active)
        val result = doRestore(backup)
        _state.emit(RestorationState.Inactive)
        return result
    }

    private suspend fun doRestore(backup: Backup): Result {
        val result = backupRepository.getContents(backup)

        return when (result) {
            is BackupRepository.Result.Success -> restoreBackup(result.value)
            is BackupRepository.Result.Failure -> Result.FetchFailure(result)
        }
    }

    private suspend fun restoreBackup(data: BackupData): Result {
        val result = backupRestorer.restore(data)

        return when (result) {
            is BackupRestorer.Result.Success -> Result.Success
            is BackupRestorer.Result.Failure -> Result.RestorationFailure(result)
        }
    }
}
