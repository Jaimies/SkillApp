package com.maxpoliakov.skillapp.backup

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.di.DaggerBackupComponent
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase

class BackupWorker (
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val localBackupUseCase = getPerformBackupUseCase(BackupBackend.Local)
        val remoteBackupUseCase = getPerformBackupUseCase(BackupBackend.GoogleDrive)

        return TODO("perform backup both locally and on google drive and return the appropriate result")
    }

    private fun getPerformBackupUseCase(backend: BackupBackend): PerformBackupUseCase {
        return DaggerBackupComponent
            .factory()
            .create(applicationContext, backend)
            .performBackupUseCase()
    }
}

