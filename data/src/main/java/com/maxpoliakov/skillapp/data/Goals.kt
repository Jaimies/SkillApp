package com.maxpoliakov.skillapp.data

import com.maxpoliakov.skillapp.domain.model.Goal

fun parseGoal(count: Long, type: Goal.Type): Goal? {
    return if (count == 0L) null
    else Goal(count, type)
}
