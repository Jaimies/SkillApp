package com.maxpoliakov.skillapp

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit.DAYS

fun clockOfEpochDay(epochDay: Long): Clock {
    return Clock.fixed(Instant.EPOCH.plus(epochDay, DAYS), ZoneId.systemDefault())
}
