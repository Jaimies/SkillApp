package com.maxpoliakov.skillapp.data.backup.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.domain.repository.BackupCreator
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase.Result.CreationFailure
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase.Result.UploadFailure
import com.maxpoliakov.skillapp.domain.usecase.backup.StubPerformBackupUseCase
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class BackupWorkerTest : StringSpec({
    fun createWorker(performBackupResult: PerformBackupUseCase.Result): BackupWorker {
        val context = mockk<Context>(relaxed = true)
        val workerParameters = mockk<WorkerParameters>(relaxed = true)
        val useCase = StubPerformBackupUseCase(performBackupResult)
        return BackupWorker(context, workerParameters, useCase)
    }

    "returns Result.success if use case returns Result.Success" {
        val worker = createWorker(performBackupResult = PerformBackupUseCase.Result.Success)
        worker.doWork() shouldBe ListenableWorker.Result.success()
    }

    "returns Result.Retry if use case returns Result.CreationFailure" {
        val worker = createWorker(
            performBackupResult = CreationFailure(BackupCreator.Result.Failure(Exception())),
        )

        worker.doWork() shouldBe ListenableWorker.Result.retry()
    }

    "returns Result.Retry if use case returns Result.UploadFailure" {
        val worker = createWorker(
            performBackupResult = UploadFailure(BackupRepository.Result.Failure.NoInternetConnection),
        )

        worker.doWork() shouldBe ListenableWorker.Result.retry()
    }
})
