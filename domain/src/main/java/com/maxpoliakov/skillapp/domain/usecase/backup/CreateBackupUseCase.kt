package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import javax.inject.Inject

class CreateBackupUseCase @Inject constructor(
    private val driveRepository: DriveRepository,
    private val backupUtil: BackupUtil,
) {
    suspend fun createBackup() {
        driveRepository.uploadBackup(backupUtil.getDatabaseBackup())
    }
}
