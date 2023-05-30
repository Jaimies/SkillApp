package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.Timer

interface NotificationUtil {
    suspend fun updateTimerNotifications(timers: List<Timer>)
}
