package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

fun Duration?.format(context: Context): String {
    if (this == null) return ""

    val hours = toHours()
    val minutesPart = toMinutesPartCompat()

    return when {
        hours == 0L -> context.getString(R.string.minutes, minutesPart.toString())
        minutesPart == 0L -> context.getString(R.string.hours, hours.toString())
        else -> context.getString(R.string.hours_and_minutes, hours, minutesPart)
    }
}

fun Context.formatGoal(goal: Goal?): String {
    if (goal == null) return getString(R.string.goal_not_set)
    val resId = if (goal.type == Goal.Type.Daily) R.string.daily_goal_without_progress
    else R.string.weekly_goal_without_progress
    return getString(resId, goal.time.format(this))
}

fun Context.formatGoal(goal: Goal?, completedTime: Duration?): String {
    if (goal == null || completedTime == null) return ""
    val stringResId = if (goal.type == Goal.Type.Daily) R.string.daily_goal else R.string.weekly_goal
    return getString(stringResId, completedTime.format(this), goal.time.format(this))
}
