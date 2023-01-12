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

class StubBackupRepository : BackupRepository {
    override suspend fun upload(data: BackupData): BackupRepository.Result<Unit> {
        delay(5)
        return BackupRepository.Result.Success(Unit)
    }

    override suspend fun getContents(backup: Backup): BackupRepository.Result<BackupData> {
        delay(5)
        return BackupRepository.Result.Success(backupContents)
    }

    override suspend fun getBackups(): List<Backup> = listOf()
    override suspend fun getLastBackup(): Backup? = null
}

class StubBackupRestorer : BackupRestorer {
    var restorationCount = 0
        private set

    override suspend fun restore(data: BackupData): BackupRestorer.Result {
        restorationCount++
        return BackupRestorer.Result.Success
    }
}

class RestoreBackupUseCaseTest : StringSpec({
    "restores backup" {
        val (useCase, backupRestorer) = createUseCase()
        useCase.restoreBackup(Backup(backupId, LocalDateTime.now()))

        backupRestorer.restorationCount shouldBe 1
    }

    "does not restore backup if another restoration is in progress" {
        val (useCase, backupRestorer) = createUseCase()

        val job1 = async { useCase.restoreBackup(backup) }
        val job2 = async { useCase.restoreBackup(backup) }

        job1.await()
        job2.await()

        backupRestorer.restorationCount shouldBe 1
    }
})

private fun createUseCase(): Pair<RestoreBackupUseCase, StubBackupRestorer> {
    val backupRepository = StubBackupRepository()
    val backupRestorer = StubBackupRestorer()

    return RestoreBackupUseCaseImpl(backupRepository, backupRestorer) to backupRestorer
}

private const val backupId = "id123"
private val backup = Backup(backupId, LocalDateTime.now())
private val backupContents = BackupData("some backup contents")
