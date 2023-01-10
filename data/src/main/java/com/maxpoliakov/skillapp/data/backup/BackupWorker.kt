package com.maxpoliakov.skillapp.data.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.data.logToCrashlytics
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val performBackupUseCase: PerformBackupUseCase,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            performBackupUseCase.performBackup()
            return Result.success()
        } catch (e: Exception) {
            e.logToCrashlytics()
            return Result.retry()
        }
    }
}

