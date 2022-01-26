package com.maxpoliakov.skillapp.ui.skilldetail

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.TimeTarget
import com.maxpoliakov.skillapp.util.ui.format
import java.time.Duration

fun Context.formatGoal(goal: TimeTarget?, completedTime: Duration?): String {
    if (goal == null || completedTime == null) return ""
    return getString(R.string.daily_goal, completedTime.format(this), goal.duration.format(this))
}
