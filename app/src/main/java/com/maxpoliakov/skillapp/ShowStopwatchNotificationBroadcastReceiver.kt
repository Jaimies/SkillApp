package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowStopwatchNotificationBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatch: Stopwatch

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ACTION_BOOT_COMPLETED)
            stopwatch.updateNotification()
    }
}
