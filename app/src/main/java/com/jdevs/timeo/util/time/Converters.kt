package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.time.HOUR_MINUTES

fun getMins(hours: Int, minutes: Int) = hours * HOUR_MINUTES + minutes
