package com.theskillapp.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.shared.extensions.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowStopwatchNotificationBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatch: Stopwatch

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ACTION_BOOT_COMPLETED) return

        goAsync {
            stopwatch.updateNotification()
        }
    }
}
