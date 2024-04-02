package com.maxpoliakov.skillapp.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.data.di.BackupBackend
import com.maxpoliakov.skillapp.di.BackupComponent
import com.maxpoliakov.skillapp.domain.usecase.backup.PerformBackupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val backupComponentFactory: BackupComponent.Factory,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val localBackupUseCase = getPerformBackupUseCase(BackupBackend.Local)
        val remoteBackupUseCase = getPerformBackupUseCase(BackupBackend.GoogleDrive)

        return TODO("perform backup both locally and on google drive and return the appropriate result")
    }

    private fun getPerformBackupUseCase(backend: BackupBackend): PerformBackupUseCase {
        return backupComponentFactory
            .create(backend)
            .performBackupUseCase()
    }
}

