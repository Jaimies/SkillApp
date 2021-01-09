package com.maxpoliakov.skillapp.util.notifications

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.shared.util.collectOnce
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtilImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val getSkill: GetSkillByIdUseCase
) : NotificationUtil {

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            createChannels()
    }

    override suspend fun showStopwatchNotification(skillId: Int) {
        getSkill.run(skillId).collectOnce { skill ->
            showNotification(skill.name)
        }
    }

    override fun removeStopwatchNotification() {
        notificationManager.cancel(STOPWATCH_NOTIFICATION_ID)
    }

    private fun showNotification(title: String) {
        val notification = NotificationCompat.Builder(context, TRACKING)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentText(title)
            .setUsesChronometer(true)
            .build()

        notificationManager.notify(STOPWATCH_NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val title = context.getString(R.string.timers)
        val channel = NotificationChannel(TRACKING, title, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val TRACKING = "com.maxpoliakov.skillapp.TRACKING"
        const val STOPWATCH_NOTIFICATION_ID = 1
    }
}
