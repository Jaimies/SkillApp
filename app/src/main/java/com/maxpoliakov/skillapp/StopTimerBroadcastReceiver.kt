package com.maxpoliakov.skillapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxpoliakov.skillapp.domain.di.ApplicationScope
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

    override fun onReceive(context: Context?, intent: Intent?) {
        val skillId = intent?.getIntExtra("skillId", -1).takeIf { it != -1 } ?: return

        scope.launch {
            stopwatch.stop(skillId)
        }
    }
}
