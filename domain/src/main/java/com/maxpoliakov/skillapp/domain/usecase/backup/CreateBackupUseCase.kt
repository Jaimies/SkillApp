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

        if (!billingRepository.subscriptionState.value.hasAccessToPremium) {
            println("Not creating a backup because the user does not have access to Premium")
            return
        }

        if (authRepository.currentUser == null) {
            println("Not creating a backup because the user is not signed in")
            return
        }

        if (!authRepository.hasAppDataPermission) {
            println("Not creating a backup because the AppData permission is not granted")
            return
        }

        driveRepository.uploadBackup(backupUtil.getDatabaseBackup())
    }
}
