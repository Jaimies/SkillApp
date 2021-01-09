package com.maxpoliakov.skillapp.util.notifications

interface NotificationUtil {
    suspend fun showStopwatchNotification(skillId: Int)
    fun removeStopwatchNotification()
}
