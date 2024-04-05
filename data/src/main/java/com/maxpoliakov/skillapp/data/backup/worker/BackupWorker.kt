package com.maxpoliakov.skillapp.data.backup.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase

open class BackupWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val performBackupUseCase: PerformBackupUseCase,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val result = performBackupUseCase.performBackup()

        if (result is PerformBackupUseCase.Result.Success) {
            return Result.success()
        } else {
            return Result.retry()
        }
    }
}
