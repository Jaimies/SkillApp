package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.repository.BackupUtil
import com.maxpoliakov.skillapp.domain.repository.DriveRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class StubDriveRepository : DriveRepository {
    override suspend fun uploadBackup(content: String) {
        delay(5)
    }

    override suspend fun getBackupContents(backup: Backup): String {
        delay(5)
        return backupContents
    }

    override suspend fun getBackups(): List<Backup> = listOf()
    override suspend fun getLastBackup(): Backup? = null
}

class CrashingStubDriveRepository : DriveRepository {
    override suspend fun uploadBackup(content: String) = throw Exception("Something went wrong")
    override suspend fun getBackups() = throw Exception("Something went wrong")
    override suspend fun getLastBackup() = throw Exception("Something went wrong")
    override suspend fun getBackupContents(backup: Backup) = throw Exception("Something went wrong")
}

class RestoreBackupUseCaseTest : StringSpec({
    "restores backup" {
        val (useCase, backupUtil) = createUseCase()
        useCase.restoreBackup(Backup(backupId, LocalDateTime.now()))

        coVerify { backupUtil.restoreBackup(backupContents) }
    }

    "does not restore backup if another restoration is in progress" {
        val (useCase, backupUtil) = createUseCase()

        val job1 = async { useCase.restoreBackup(backup) }
        val job2 = async { useCase.restoreBackup(backup) }

        job1.await()
        job2.await()

        coVerify(exactly = 1) { backupUtil.restoreBackup(backupContents) }
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
        val (useCase) = createUseCase(CrashingStubDriveRepository())
        shouldThrow<Exception> { useCase.restoreBackup(backup) }
        useCase.state.value shouldBe RestorationState.Failed
    }
})

private fun createUseCase(driveRepository: DriveRepository = StubDriveRepository()): Pair<RestoreBackupUseCase, BackupUtil> {
    val backupUtil = mockk<BackupUtil>(relaxed = true)
    return RestoreBackupUseCase(driveRepository, backupUtil) to backupUtil
}

private const val backupContents = "some backup contents"
private const val backupId = "id123"
private val backup = Backup(backupId, LocalDateTime.now())
