package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import javax.inject.Inject

class CreateBackupUseCase @Inject constructor(
    private val driveRepository: DriveRepository,
    private val authRepository: AuthRepository,
    private val billingRepository: BillingRepository,
    private val backupUtil: BackupUtil,
) {
    suspend fun createBackup() {
        billingRepository.connect()

        if (authRepository.currentUser == null) {
            println("Not creating a backup because the user is not signed in")
            return
        }

        if (!billingRepository.isSubscribed.value) {
            println("Not creating a backup because the user is not subscribed")
            return
        }

        driveRepository.uploadBackup(backupUtil.getDatabaseBackup())
    }
}
