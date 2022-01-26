package com.maxpoliakov.skillapp.ui.skilldetail

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.TimeTarget
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

fun Context.formatGoal(goal: TimeTarget?): String {
    if (goal == null) return ""
    return getString(R.string.daily_goal, Duration.ofHours(3).format(this), goal.duration.format(this))
}

private fun Duration.format(context: Context): String {
    val hours = toHours()
    val minutesPart = toMinutesPartCompat()

    return when {
        hours == 0L -> context.getString(R.string.minutes, minutesPart.toString())
        minutesPart == 0L -> context.getString(R.string.hours, hours.toString())
        else -> context.getString(R.string.hours_and_minutes, hours, minutesPart)
    }
}
