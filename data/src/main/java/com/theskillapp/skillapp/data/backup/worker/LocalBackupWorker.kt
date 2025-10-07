package com.theskillapp.skillapp.data.backup.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.theskillapp.skillapp.data.di.Local
import com.theskillapp.skillapp.data.di.BackupComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class LocalBackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    @Local
    private val backupComponent: BackupComponent,
): BackupWorker(context, workerParameters, backupComponent.performBackupUseCase(), backupComponent.configuration())
