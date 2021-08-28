package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestoreBackupUseCase @Inject constructor(
    private val driveRepository: DriveRepository,
    private val backupUtil: BackupUtil,
) {
    private val _isRestorationInProgress = MutableStateFlow(false)
    val isRestorationInProgress: StateFlow<Boolean> get() = _isRestorationInProgress

    suspend fun restoreBackup(backup: Backup) {
        if (isRestorationInProgress.value) return

        _isRestorationInProgress.emit(true)

        val backupContents = driveRepository.getBackupContents(backup)
        backupUtil.restoreBackup(backupContents)

        _isRestorationInProgress.emit(false)
    }
}
