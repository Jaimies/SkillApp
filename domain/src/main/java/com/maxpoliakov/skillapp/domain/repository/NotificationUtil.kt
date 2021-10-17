package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.StopwatchState

interface NotificationUtil {
    fun showStopwatchNotification(state: StopwatchState.Running)
    fun removeStopwatchNotification()
}
