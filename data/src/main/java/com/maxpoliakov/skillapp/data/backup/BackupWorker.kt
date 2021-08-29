package com.maxpoliakov.skillapp.data.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val createBackupUseCase: CreateBackupUseCase,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            createBackupUseCase.createBackup()
            return Result.success()
        } catch (e: Exception) {
            val dataBuilder = Data.Builder()
            if (e.message != null) dataBuilder.putString("errorMessage", e.message)
            return Result.failure(dataBuilder.build())
        }
    }
}

