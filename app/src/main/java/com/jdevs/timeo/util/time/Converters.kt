package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.time.HOUR_MINUTES

fun getMins(hours: Long, minutes: Long) = hours * HOUR_MINUTES + minutes
