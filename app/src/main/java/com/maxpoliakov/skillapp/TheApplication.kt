package com.maxpoliakov.skillapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.WorkRequest.Companion.DEFAULT_BACKOFF_DELAY_MILLIS
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.maxpoliakov.skillapp.data.backup.worker.LocalBackupWorker
import com.maxpoliakov.skillapp.shared.extensions.setupTheme
import dagger.hilt.android.HiltAndroidApp
import java.time.Duration
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

@HiltAndroidApp
open class TheApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()
        setupTheme()
        setupBackupWorker()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    private fun setupBackupWorker() {
        setupWorker<LocalBackupWorker>(SHARED_STORAGE_BACKUP_WORKER_TAG)
    }

    private inline fun <reified W: ListenableWorker> setupWorker(
        name: String,
        constraintBuilderAction: Constraints.Builder.() -> Unit = {},
    ) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .apply(constraintBuilderAction)
            .build()

        val backupWorkRequest = PeriodicWorkRequestBuilder<W>(Duration.ofDays(1))
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.LINEAR, DEFAULT_BACKOFF_DELAY_MILLIS, MILLISECONDS)
            .build()

        workManager
            .enqueueUniquePeriodicWork(name, ExistingPeriodicWorkPolicy.KEEP, backupWorkRequest)
    }

    companion object {
        private const val SHARED_STORAGE_BACKUP_WORKER_TAG = "com.maxpoliakov.skillapp.SHARED_STORAGE_BACKUP_WORKER_TAG"
    }
}
