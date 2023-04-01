package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.stopwatch.Stopwatch

interface NotificationUtil {
    fun showStopwatchNotification(state: Stopwatch.State.Running)
    fun removeStopwatchNotification()
}
