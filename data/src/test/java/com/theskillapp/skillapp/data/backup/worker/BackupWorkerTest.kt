package com.theskillapp.skillapp.data.backup.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.theskillapp.skillapp.domain.model.GenericUri
import com.theskillapp.skillapp.domain.repository.BackupCreator
import com.theskillapp.skillapp.domain.repository.BackupRepository
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase.Result.CreationFailure
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase.Result.UploadFailure
import com.theskillapp.skillapp.domain.usecase.backup.StubPerformBackupUseCase
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class StubConfigurationManager(
    configuration: Configuration = Configuration.Success(GenericUri("uri")),
): BackupConfigurationManager {
    override val configuration = flowOf(configuration)
    override fun handleException(throwable: Throwable) = BackupRepository.Result.Failure.Error(throwable)
}

class BackupWorkerTest : StringSpec({
    fun createWorker(
        performBackupResult: PerformBackupUseCase.Result,
        configuration: Configuration = Configuration.Success(GenericUri("abc")),
    ): BackupWorker {
        val context = mockk<Context>(relaxed = true)
        val workerParameters = mockk<WorkerParameters>(relaxed = true)
        val useCase = StubPerformBackupUseCase(performBackupResult)
        val configurationManager = StubConfigurationManager(configuration)
        return BackupWorker(context, workerParameters, useCase, configurationManager)
    }

    // not configured -> no need to back up -> no problem

    "returns Result.success and does nothing if not configured" {
        val worker = createWorker(
            PerformBackupUseCase.Result.Success,
            Configuration.Failure(BackupRepository.Result.Failure.NotConfigured),
        )

        worker.doWork() shouldBe ListenableWorker.Result.success()
    }

    "return Result.success if backup failed because of repository not being configured" {
        val worker = createWorker(
            PerformBackupUseCase.Result.UploadFailure(BackupRepository.Result.Failure.NotConfigured)
        )

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
            performBackupResult = UploadFailure(BackupRepository.Result.Failure.Error(Exception())),
        )

        worker.doWork() shouldBe ListenableWorker.Result.retry()
    }

    "returns Result.success if use case returns Result.Success" {
        val worker = createWorker(performBackupResult = PerformBackupUseCase.Result.Success)
        worker.doWork() shouldBe ListenableWorker.Result.success()
    }
})
