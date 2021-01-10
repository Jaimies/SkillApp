package com.maxpoliakov.skillapp

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import com.maxpoliakov.skillapp.util.stopwatch.StopwatchUtil
import io.kotest.core.spec.style.StringSpec
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions

class ShowStopwatchNotificationBroadcastReceiverTest : StringSpec({
    val context = mock(Context::class.java)

    fun createIntent(action: String): Intent {
        return mock(Intent::class.java).apply {
            `when`(this.action).thenReturn(action)
        }
    }

    fun createReceiver(): ShowStopwatchNotificationBroadcastReceiver {
        return ShowStopwatchNotificationBroadcastReceiver().apply {
            stopwatchUtil = mock(StopwatchUtil::class.java)
        }
    }

    "updates the notification if the action is ACTION_BOOT_COMPLETED" {
        val receiver = createReceiver()
        receiver.onReceive(context, createIntent(ACTION_BOOT_COMPLETED))
        verify(receiver.stopwatchUtil).updateNotification()
    }

    "doesn't update the notification if the action is not ACTION_BOOT_COMPLETED" {
        val receiver = createReceiver()
        receiver.onReceive(context, createIntent("otherAction"))
        verifyNoInteractions(receiver.stopwatchUtil)
    }
})
