package com.maxpoliakov.skillapp.domain.usecase.backup

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PerformBackupUseCaseTest : StringSpec({
    "returns Result.Success when successful" {
        val backupRepository = StubBackupRepository()
        val backupCreator = StubBackupCreator()

        val useCase = PerformBackupUseCaseImpl(backupRepository, backupCreator)

        useCase.performBackup() shouldBe PerformBackupUseCase.Result.Success
    }

    "returns Result.CreationFailure when BackupCreator fails" {
        val backupRepository = StubBackupRepository()
        val backupCreator = StubBackupCreator(result = backupCreationFailure)

        val useCase = PerformBackupUseCaseImpl(backupRepository, backupCreator)

        useCase.performBackup() shouldBe PerformBackupUseCase.Result.CreationFailure(backupCreationFailure)
    }

    "returns Result.UploadFailure when BackupRepository.upload() fails" {
        val backupRepository = StubBackupRepository(uploadResult = backupRepositoryFailure)
        val backupCreator = StubBackupCreator()

        val useCase = PerformBackupUseCaseImpl(backupRepository, backupCreator)

        useCase.performBackup() shouldBe PerformBackupUseCase.Result.UploadFailure(backupRepositoryFailure)
    }
})
