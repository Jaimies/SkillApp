package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.Timer

interface NotificationUtil {
    suspend fun updateTimerNotifications(timers: List<Timer>)
}
