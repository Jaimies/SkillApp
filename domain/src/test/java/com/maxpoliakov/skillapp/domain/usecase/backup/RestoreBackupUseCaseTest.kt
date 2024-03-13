package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class RestoreBackupUseCaseTest : StringSpec({
    "restores backup" {
        val backupRepository = StubBackupRepository()
        val backupRestorer = StubBackupRestorer()

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        useCase.restoreBackup(Backup(backupUri, LocalDateTime.now()))

        backupRestorer.restorationCount shouldBe 1
    }

    "does not restore backup and returns RestoreBackupUseCase.Result.AlreadyInProgress if another restoration is in progress" {
        val backupRepository = StubBackupRepository()
        val backupRestorer = StubBackupRestorer()

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        val job = async { useCase.restoreBackup(backup) }
        delay(1)
        useCase.restoreBackup(backup) shouldBe RestoreBackupUseCase.Result.AlreadyInProgress
        job.await()

        backupRestorer.restorationCount shouldBe 1
    }

    "returns Result.Success when successful" {
        val backupRepository = StubBackupRepository()
        val backupRestorer = StubBackupRestorer()

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        useCase.restoreBackup(backup) shouldBe RestoreBackupUseCase.Result.Success
    }

    "returns Result.RestorationFailure when BackupRestorer fails" {
        val backupRepository = StubBackupRepository()
        val backupRestorer = StubBackupRestorer(result = backupRestorationFailure)

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        useCase.restoreBackup(backup) shouldBe RestoreBackupUseCase.Result.RestorationFailure(backupRestorationFailure)
    }

    "returns Result.FetchFailure when BackupRepository.getContents() fails" {
        val backupRepository = StubBackupRepository(getContentsResult = backupRepositoryFailure)
        val backupRestorer = StubBackupRestorer()

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        useCase.restoreBackup(backup) shouldBe RestoreBackupUseCase.Result.FetchFailure(backupRepositoryFailure)
    }
})
