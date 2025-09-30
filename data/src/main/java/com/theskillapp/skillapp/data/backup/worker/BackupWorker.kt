package com.theskillapp.skillapp.data.backup.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.theskillapp.skillapp.domain.usecase.backup.PerformBackupUseCase
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager
import com.theskillapp.skillapp.data.backup.BackupConfigurationManager.Configuration
import com.theskillapp.skillapp.domain.repository.BackupRepository
import kotlinx.coroutines.flow.first

open class BackupWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val performBackupUseCase: PerformBackupUseCase,
    private val configurationManager: BackupConfigurationManager,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        if (configurationManager.configuration.first() is Configuration.Failure) {
            // not configured - don't back up
            return Result.success()
        }

        val result = performBackupUseCase.performBackup()

        if (result is PerformBackupUseCase.Result.Success
            || (result is PerformBackupUseCase.Result.UploadFailure 
            && result.uploadResult is BackupRepository.Result.Failure.NotConfigured)) {
            return Result.success()
        } else {
            return Result.retry()
        }
    }
}
