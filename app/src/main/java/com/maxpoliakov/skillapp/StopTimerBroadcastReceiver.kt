package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxpoliakov.skillapp.di.coroutines.ApplicationScope
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopTimerBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatch: Stopwatch

    @Inject
    @ApplicationScope
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var notificationUtil: NotificationUtil

    override fun onReceive(context: Context?, intent: Intent?) {
        scope.launch {
            stopwatch.stop()
            delay(100)
            notificationUtil.removeStopwatchNotification()
        }
    }
}
