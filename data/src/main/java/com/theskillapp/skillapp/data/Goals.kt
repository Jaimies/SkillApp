package com.theskillapp.skillapp.data

import com.theskillapp.skillapp.domain.model.Goal

fun parseGoal(count: Long, type: Goal.Type): Goal? {
    return if (count == 0L) null
    else Goal(count, type)
}
