package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.domain.model.Goal
import java.time.Duration

fun parseGoal(time: Duration, type: Goal.Type): Goal? {
    return if (time == Duration.ZERO) null
    else Goal(time, type)
}
