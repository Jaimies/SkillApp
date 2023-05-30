package com.maxpoliakov.skillapp.shared.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.StopTimerBroadcastReceiver
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.NotificationUtil
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.shared.bindingadapters.chronometerBase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtilImpl @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val getSkill: GetSkillByIdUseCase,
) : NotificationUtil {

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            createChannels()
    }

    override suspend fun updateTimerNotifications(timers: List<Timer>) {
        cancelNotificationsForStoppedTimers(timers)
        timers.forEach { it.showNotification() }
    }

    private fun cancelNotificationsForStoppedTimers(activeTimers: List<Timer>) {
        val activeTimerNotificationIds = activeTimers.map { it.getNotificationId() }

        notificationManager.activeNotifications
            .filterNot { it.id in activeTimerNotificationIds }
            .forEach { notificationManager.cancel(it.id) }
    }

    private suspend fun Timer.showNotification() {
        showNotification(getSkill.run(skillId).first())
    }

    private fun Timer.showNotification(skill: Skill) {
        val notification = NotificationCompat.Builder(context, TRACKING)
            .setOngoing(true)
            .setSmallIcon(R.drawable.notification_icon)
            .setShowWhen(false)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(getContentView(skill))
            .setContentIntent(getContentIntent())
            .setSilent(true)
            .addAction(R.drawable.ic_check, context.getString(R.string.stop), getStopTimerIntent())
            .build()

        // will likely work without the try/catch,
        // but it's better to be safe than sorry
        try {
            notificationManager.notify(getNotificationId(), notification)
        } catch(e: SecurityException) {}
    }

    private fun Timer.getContentView(skill: Skill): RemoteViews {
        return RemoteViews(context.packageName, R.layout.notification).apply {
            setTextViewText(R.id.title, skill.name)
            setChronometer(R.id.chronometer, startTime.chronometerBase, null, true)
        }
    }

    private fun Timer.getContentIntent(): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main)
            .setDestination(R.id.skill_detail_fragment_dest)
            .setArguments(bundleOf("skillId" to skillId))
            .createPendingIntent()
    }

    private fun Timer.getStopTimerIntent(): PendingIntent {
        val intent = Intent(context, StopTimerBroadcastReceiver::class.java).apply {
            putExtra("skillId", skillId)
        }

        return PendingIntent.getBroadcast(context, getNotificationId(), intent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
    }

    private fun Timer.getNotificationId() = skillId

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val title = context.getString(R.string.timers)
        val channel = NotificationChannel(TRACKING, title, IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val TRACKING = "com.maxpoliakov.skillapp.TRACKING"
    }
}
