package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StopTimerBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatchUtil: StopwatchUtil

    override fun onReceive(context: Context?, intent: Intent?) {
        stopwatchUtil.stop()
    }
}
