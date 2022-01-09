package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopTimerBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatchUtil: StopwatchUtil

    @Inject
    lateinit var ioScope: CoroutineScope

    @Inject
    lateinit var notificationUtil: NotificationUtil

    override fun onReceive(context: Context?, intent: Intent?) {
        ioScope.launch {
            stopwatchUtil.stop()
            delay(100)
            notificationUtil.removeStopwatchNotification()
        }
    }
}
