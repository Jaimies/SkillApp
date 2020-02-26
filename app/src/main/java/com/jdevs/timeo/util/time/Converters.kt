package com.jdevs.timeo.util.time

import com.jdevs.timeo.shared.time.HOUR_MINUTES

fun getMins(hour: Int, minute: Int) = hour * HOUR_MINUTES + minute
