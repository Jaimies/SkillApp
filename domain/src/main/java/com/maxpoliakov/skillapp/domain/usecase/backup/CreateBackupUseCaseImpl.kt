package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import javax.inject.Inject

class CreateBackupUseCaseImpl @Inject constructor(
    private val backupRepository: BackupRepository,
    private val authRepository: AuthRepository,
    private val backupCreator: BackupCreator,
): CreateBackupUseCase {
    override suspend fun createBackup() {
        if (authRepository.currentUser == null) {
            println("Not creating a backup because the user is not signed in")
            return
        }

        if (!authRepository.hasAppDataPermission) {
            println("Not creating a backup because the AppData permission is not granted")
            return
        }

        backupRepository.upload(backupCreator.create())
    }
}
