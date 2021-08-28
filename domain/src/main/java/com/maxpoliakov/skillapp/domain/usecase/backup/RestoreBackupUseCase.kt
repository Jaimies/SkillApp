package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import javax.inject.Inject

class RestoreBackupUseCase @Inject constructor(
    private val driveRepository: DriveRepository,
    private val backupUtil: BackupUtil,
) {
    suspend fun restoreBackup(backup: Backup) {
        val backupContents = driveRepository.getBackupContents(backup)
        backupUtil.restoreBackup(backupContents)
    }
}
