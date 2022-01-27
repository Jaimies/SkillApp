package com.maxpoliakov.skillapp.ui.skilldetail

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.util.ui.format
import java.time.Duration

fun Context.formatGoal(goal: Goal?, completedTime: Duration?): String {
    if (goal == null || completedTime == null) return ""
    return getString(R.string.daily_goal, completedTime.format(this), goal.time.format(this))
}
