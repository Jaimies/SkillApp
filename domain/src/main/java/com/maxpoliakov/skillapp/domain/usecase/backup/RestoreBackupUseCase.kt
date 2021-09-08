package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class RestorationState {
    NotStarted, Active, Finished, Failed
}

@Singleton
class RestoreBackupUseCase @Inject constructor(
    private val driveRepository: DriveRepository,
    private val backupUtil: BackupUtil,
) {
    private val _state = MutableStateFlow(RestorationState.NotStarted)
    val state: StateFlow<RestorationState> get() = _state

    suspend fun restoreBackup(backup: Backup) {
        if (state.value == RestorationState.Active) return

        _state.emit(RestorationState.Active)

        try {
            val backupContents = driveRepository.getBackupContents(backup)
            backupUtil.restoreBackup(backupContents)
            _state.emit(RestorationState.Finished)
        } catch (e: Exception) {
            _state.emit(RestorationState.Failed)
            throw e
        }
    }
}
