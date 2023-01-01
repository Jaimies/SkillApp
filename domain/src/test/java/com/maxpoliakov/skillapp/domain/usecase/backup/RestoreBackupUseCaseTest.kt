package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.model.result.BackupUploadResult
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import com.maxpoliakov.skillapp.domain.usecase.backup.RestoreBackupUseCase.RestorationState
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class StubBackupRepository : BackupRepository {
    override suspend fun upload(data: BackupData): BackupUploadResult {
        delay(5)
        return BackupUploadResult.Success
    }

    override suspend fun getContents(backup: Backup): BackupData {
        delay(5)
        return backupContents
    }

    override suspend fun getBackups(): List<Backup> = listOf()
    override suspend fun getLastBackup(): Backup? = null
}

class CrashingStubBackupRepository : BackupRepository {
    override suspend fun upload(data: BackupData) = throw Exception("Something went wrong")
    override suspend fun getBackups() = throw Exception("Something went wrong")
    override suspend fun getLastBackup() = throw Exception("Something went wrong")
    override suspend fun getContents(backup: Backup) = throw Exception("Something went wrong")
}

class RestoreBackupUseCaseTest : StringSpec({
    "restores backup" {
        val (useCase, backupRestorer) = createUseCase()
        useCase.restoreBackup(Backup(backupId, LocalDateTime.now()))

        coVerify { backupRestorer.restore(backupContents) }
    }

    "does not restore backup if another restoration is in progress" {
        val (useCase, backupRestorer) = createUseCase()

        val job1 = async { useCase.restoreBackup(backup) }
        val job2 = async { useCase.restoreBackup(backup) }

        job1.await()
        job2.await()

        coVerify(exactly = 1) { backupRestorer.restore(backupContents) }
    }

    "updates state" {
        val (useCase) = createUseCase()

        useCase.state.value shouldBe RestorationState.NotStarted
        val job = async { useCase.restoreBackup(backup) }

        delay(1)
        useCase.state.value shouldBe RestorationState.Active

        job.await()
        useCase.state.value shouldBe RestorationState.Finished
    }

    "updates state to Failed when failing" {
        val (useCase) = createUseCase(CrashingStubBackupRepository())
        shouldThrow<Exception> { useCase.restoreBackup(backup) }
        useCase.state.value shouldBe RestorationState.Failed
    }

    "does not restore backup if the AppData permission was not granted" {
        val (useCase, backupRestorer) = createUseCase(hasAppDataPermission = false)
        useCase.restoreBackup(backup)
        coVerify(exactly = 0) { backupRestorer.restore(backupContents) }
    }
})

private fun createUseCase(
    backupRepository: BackupRepository = StubBackupRepository(),
    hasAppDataPermission: Boolean = true
): Pair<RestoreBackupUseCase, BackupRestorer> {
    val backupRestorer = mockk<BackupRestorer>(relaxed = true)
    val authRepository = mockk<AuthRepository>(relaxed = true)

    every { authRepository.hasAppDataPermission } returns hasAppDataPermission

    return RestoreBackupUseCaseImpl(backupRepository, backupRestorer, authRepository) to backupRestorer
}

private const val backupId = "id123"
private val backup = Backup(backupId, LocalDateTime.now())
private val backupContents = BackupData("some backup contents")
