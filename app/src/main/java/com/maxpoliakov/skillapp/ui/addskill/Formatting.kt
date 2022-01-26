package com.maxpoliakov.skillapp.ui.addskill

import android.content.Context
import com.maxpoliakov.skillapp.util.ui.format
import java.time.Duration

fun Context.formatDailyGoal(time: Duration?): String {
    if (time == null || time == Duration.ZERO) return "Goal not set"
    return time.format(this)
}
