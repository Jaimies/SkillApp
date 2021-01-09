package com.maxpoliakov.skillapp.util.notifications

import com.maxpoliakov.skillapp.util.stopwatch.StopwatchState

interface NotificationUtil {
    fun showStopwatchNotification(state: StopwatchState.Running)
    fun removeStopwatchNotification()
}
