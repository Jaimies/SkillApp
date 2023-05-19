package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Timer

interface NotificationUtil {
    fun showStopwatchNotification(timer: Timer)
    fun removeStopwatchNotification()
}
