package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopTimerBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatchUtil: StopwatchUtil

    @Inject
    lateinit var ioScope: CoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        ioScope.launch {
            stopwatchUtil.stop()
        }
    }
}
