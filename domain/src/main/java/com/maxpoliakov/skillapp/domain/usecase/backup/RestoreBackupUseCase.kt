package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
) {
    private val _state = MutableStateFlow(RestorationState.NotStarted)
    val state: StateFlow<RestorationState> get() = _state

    suspend fun restoreBackup(backup: Backup) {
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
            val backupContents = driveRepository.getBackupContents(backup)
            backupUtil.restoreBackup(backupContents)
            _state.emit(RestorationState.Finished)
        } catch (e: Exception) {
            _state.emit(RestorationState.Failed)
            throw e
        }
    }
}
