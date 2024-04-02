package com.maxpoliakov.skillapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.WorkRequest.Companion.DEFAULT_BACKOFF_DELAY_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maxpoliakov.skillapp.backup.BackupWorker
import com.maxpoliakov.skillapp.shared.extensions.setupTheme
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

@HiltAndroidApp
open class TheApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        setupTheme()
        setupBackupWorker()
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
            .setBackoffCriteria(BackoffPolicy.LINEAR, DEFAULT_BACKOFF_DELAY_MILLIS, MILLISECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("createBackup", ExistingPeriodicWorkPolicy.KEEP, backupWorkRequest)
    }
}
