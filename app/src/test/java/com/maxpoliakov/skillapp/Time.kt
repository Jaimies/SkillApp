package com.maxpoliakov.skillapp

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit.DAYS

fun clockOfEpochDay(epochDay: Long): Clock {
    return clockOfInstant(Instant.EPOCH.plus(epochDay, DAYS))
}

fun clockOfEpochSecond(second: Long): Clock {
    return clockOfInstant(Instant.ofEpochSecond(second))
}

fun clockOfInstant(instant: Instant): Clock {
    return Clock.fixed(instant, ZoneId.systemDefault())
}
