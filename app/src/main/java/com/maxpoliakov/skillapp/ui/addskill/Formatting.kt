package com.maxpoliakov.skillapp.ui.addskill

import android.content.Context
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.util.ui.format

fun Context.formatDailyGoal(goal: Goal?): String {
    if (goal == null) return "Goal not set"
    val goalType = if (goal.type == Goal.Type.Daily) "Daily" else "Weekly"
    return goalType + " goal: " + goal.time.format(this)
}
