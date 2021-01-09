package com.maxpoliakov.skillapp

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.util.notifications.NotificationUtil
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Paused
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState.Running
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.flow.MutableStateFlow
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions

class ShowStopwatchNotificationBroadcastReceiverTest : StringSpec({
    val skillId = 12
    val runningState = Running(getZonedDateTime(), skillId)
    val context = mock(Context::class.java)

    fun createIntent(action: String): Intent {
        return mock(Intent::class.java).apply {
            `when`(this.action).thenReturn(action)
        }
    }

    fun createReceiver(state: StopwatchState): ShowStopwatchNotificationBroadcastReceiver {
        return ShowStopwatchNotificationBroadcastReceiver().apply {
            notificationUtil = mock(NotificationUtil::class.java)
            stopwatchUtil = mock(StopwatchUtil::class.java)
            `when`(stopwatchUtil.state).thenReturn(MutableStateFlow(state))
        }
    }

    "shows the stopwatch if the state is Running" {
        val receiver = createReceiver(runningState)
        receiver.onReceive(context, createIntent(ACTION_BOOT_COMPLETED))
        verify(receiver.notificationUtil).showStopwatchNotification(runningState)
    }

    "doesn't show the stopwatch if the state is Paused" {
        val receiver = createReceiver(Paused)
        receiver.onReceive(context, createIntent(ACTION_BOOT_COMPLETED))
        verifyNoInteractions(receiver.notificationUtil)
    }

    "doesn't show the stopwatch if the action is not ACTION_BOOT_COMPLETED" {
        val receiver = createReceiver(runningState)
        receiver.onReceive(context, createIntent("otherAction"))
        verifyNoInteractions(receiver.notificationUtil)
    }
})
