package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
): RestoreBackupUseCase {
    private val _state = MutableStateFlow(RestorationState.NotStarted)
    override val state: StateFlow<RestorationState> get() = _state

    override suspend fun restoreBackup(backup: Backup) {
        if (!authRepository.hasAppDataPermission) {
            println("Not restoring a backup because the AppData permission is not granted")
            return
        }

        if (state.value == RestorationState.Active) {
            println("Not starting a backup restoration because another restoration is already running")
            return
        }

        _state.emit(RestorationState.Active)

        try {
            val backupContents = backupRepository.getBackupContents(backup)
            backupRestorer.restore(backupContents)
            _state.emit(RestorationState.Finished)
        } catch (e: Exception) {
            _state.emit(RestorationState.Failed)
            throw e
        }
    }
}
