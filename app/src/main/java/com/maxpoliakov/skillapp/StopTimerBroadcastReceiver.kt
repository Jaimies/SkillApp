package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch
import com.maxpoliakov.skillapp.shared.extensions.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StopTimerBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var stopwatch: Stopwatch

    override fun onReceive(context: Context?, intent: Intent?) {
        val skillId = intent?.getIntExtra("skillId", -1).takeIf { it != -1 } ?: return

        goAsync {
            stopwatch.stop(skillId)
        }
    }
}
