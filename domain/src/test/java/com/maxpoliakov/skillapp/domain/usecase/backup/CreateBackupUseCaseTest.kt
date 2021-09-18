package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

class CreateBackupUseCaseTest : StringSpec({
    "creates backup if both authenticated and subscribed" {
        val (useCase, driveRepository) = createUseCase(isSubscribed = true, isAuthenticated = true)
        useCase.createBackup()
        coVerify { driveRepository.uploadBackup(backupData) }
    }
    "doesn't create backup if not authenticated" {
        val (useCase, driveRepository) = createUseCase(isSubscribed = true, isAuthenticated = false)
        useCase.createBackup()
        coVerify(exactly = 0) { driveRepository.uploadBackup(any()) }
    }
    "doesn't create backup if not subscribed" {
        val (useCase, driveRepository) = createUseCase(isSubscribed = false, isAuthenticated = true)
        useCase.createBackup()
        coVerify(exactly = 0) { driveRepository.uploadBackup(backupData) }
    }
    "doesn't create backup if neither subscribed nor authenticated" {
        val (useCase, driveRepository) = createUseCase(isSubscribed = false, isAuthenticated = false)
        useCase.createBackup()
        coVerify(exactly = 0) { driveRepository.uploadBackup(backupData) }
    }
})

private fun createUseCase(isSubscribed: Boolean, isAuthenticated: Boolean): Pair<CreateBackupUseCase, DriveRepository> {
    val driveRepository = mockk<DriveRepository>(relaxed = true)
    val authRepository = mockk<AuthRepository>(relaxed = true)
    val billingRepository = mockk<BillingRepository>(relaxed = true)
    val backupUtil = mockk<BackupUtil>(relaxed = true)

    coEvery { backupUtil.getDatabaseBackup() } returns backupData
    every { billingRepository.isSubscribed } returns MutableStateFlow(isSubscribed)
    every { authRepository.currentUser } returns if (isAuthenticated) User("user@gmail.com") else null

    return CreateBackupUseCase(driveRepository, authRepository, billingRepository, backupUtil) to driveRepository
}

private const val backupData = "Some backup data"