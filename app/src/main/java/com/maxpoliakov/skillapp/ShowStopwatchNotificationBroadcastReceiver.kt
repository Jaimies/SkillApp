package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShowStopwatchNotificationBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationUtil: NotificationUtil

    @Inject
    lateinit var stopwatchUtil: StopwatchUtil

    override fun onReceive(context: Context?, intent: Intent?) {
        val stopwatchState = stopwatchUtil.state.value
        if (intent?.action == ACTION_BOOT_COMPLETED && stopwatchState is Running)
            notificationUtil.showStopwatchNotification(stopwatchState)
    }
}
