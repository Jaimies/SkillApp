package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.time.HOUR_MINUTES

fun getHours(totalMinutes: Int) =
    getHours(totalMinutes.toLong())

fun getMins(hours: Long, minutes: Long) = hours * HOUR_MINUTES + minutes
