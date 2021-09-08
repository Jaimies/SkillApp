package com.maxpoliakov.skillapp.data.backup

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maxpoliakov.skillapp.data.billing.BillingRepository
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val createBackupUseCase: CreateBackupUseCase,
    private val authRepository: AuthRepository,
    private val billingRepository: BillingRepository,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        try {
            billingRepository.connect()

            if (authRepository.currentUser != null && billingRepository.isSubscribed.value)
                createBackupUseCase.createBackup()

            return Result.success()
        } catch (e: Exception) {
            return Result.retry()
        }
    }
}

