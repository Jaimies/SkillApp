package com.maxpoliakov.skillapp.util.notifications

import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.StopTimerBroadcastReceiver
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.usecase.skill.GetSkillByIdUseCase
import com.maxpoliakov.skillapp.shared.util.collectOnce
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationUtilImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat,
    private val getSkill: GetSkillByIdUseCase,
    private val scope: CoroutineScope
) : NotificationUtil {

    init {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            createChannels()
    }

    override fun showStopwatchNotification(skillId: Int) {
        scope.launch {
            getSkill.run(skillId).collectOnce { skill ->
                showNotification(skill)
            }
        }
    }

    override fun removeStopwatchNotification() {
        notificationManager.cancel(STOPWATCH_NOTIFICATION_ID)
    }

    private fun showNotification(skill: Skill) {
        val notification = NotificationCompat.Builder(context, TRACKING)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentText(skill.name)
            .setUsesChronometer(true)
            .setContentIntent(getContentIntent(skill.id))
            .addAction(R.drawable.ic_check, context.getString(R.string.stop), getStopTimerIntent())
            .build()

        notificationManager.notify(STOPWATCH_NOTIFICATION_ID, notification)
    }

    private fun getContentIntent(skillId: Int): PendingIntent {
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main)
            .setDestination(R.id.skill_detail_fragment_dest)
            .setArguments(bundleOf("skillId" to skillId))
            .createPendingIntent()
    }

    private fun getStopTimerIntent(): PendingIntent {
        val intent = Intent(context, StopTimerBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, STOP_INTENT_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT)
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
        const val STOP_INTENT_REQUEST_CODE = 1
    }
}
