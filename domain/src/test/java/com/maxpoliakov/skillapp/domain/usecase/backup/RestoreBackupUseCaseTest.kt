package com.maxpoliakov.skillapp.domain.usecase.backup

import com.maxpoliakov.skillapp.domain.model.Backup
import com.maxpoliakov.skillapp.domain.model.BackupData
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRestorer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class StubBackupRepository(
    private val uploadResult: BackupRepository.Result<Unit> = uploadSuccess,
    private val getContentsResult: BackupRepository.Result<BackupData> = getContentsSuccess,
) : BackupRepository {
    override suspend fun upload(data: BackupData): BackupRepository.Result<Unit> {
        delay(2)
        return uploadResult
    }

    override suspend fun getContents(backup: Backup): BackupRepository.Result<BackupData> {
        delay(2)
        return getContentsResult
    }

    override suspend fun getBackups(): List<Backup> = listOf()
    override suspend fun getLastBackup(): Backup? = null
}

class StubBackupRestorer(
    private val result: BackupRestorer.Result = BackupRestorer.Result.Success,
) : BackupRestorer {
    var restorationCount = 0
        private set

    override suspend fun restore(data: BackupData): BackupRestorer.Result {
        restorationCount++
        return result
    }
}

class RestoreBackupUseCaseTest : StringSpec({
    "restores backup" {
        val backupRepository = StubBackupRepository()
        val backupRestorer = StubBackupRestorer()

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        useCase.restoreBackup(Backup(backupId, LocalDateTime.now()))

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
        val backupRepository = StubBackupRepository(getContentsResult = getContentsFailure)
        val backupRestorer = StubBackupRestorer()

        val useCase = RestoreBackupUseCaseImpl(backupRepository, backupRestorer)

        useCase.restoreBackup(backup) shouldBe RestoreBackupUseCase.Result.FetchFailure(getContentsFailure)
    }
})

private const val backupId = "id123"
private val backup = Backup(backupId, LocalDateTime.now())
private val backupData = BackupData("some backup contents")

private val backupRestorationFailure = BackupRestorer.Result.Failure(Error("Error"))
private val getContentsFailure = BackupRepository.Result.Failure.Error(Error("Error"))
private val uploadSuccess = BackupRepository.Result.Success(Unit)
private val getContentsSuccess = BackupRepository.Result.Success(backupData)

