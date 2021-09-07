package com.maxpoliakov.skillapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest.MIN_BACKOFF_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maxpoliakov.skillapp.billing.BillingRepository
import com.maxpoliakov.skillapp.data.backup.BackupWorker
import com.maxpoliakov.skillapp.util.ui.setupTheme
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

@HiltAndroidApp
open class TheApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var billingRepository: BillingRepository

    @Inject
    lateinit var ioScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        setupTheme()
        setupBackupWorker()
        ioScope.launch { billingRepository.connect() }
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    private fun setupBackupWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val backupWorkRequest = PeriodicWorkRequestBuilder<BackupWorker>(Duration.ofDays(1))
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, MIN_BACKOFF_MILLIS, MILLISECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("createBackup", ExistingPeriodicWorkPolicy.KEEP, backupWorkRequest)
    }
}
