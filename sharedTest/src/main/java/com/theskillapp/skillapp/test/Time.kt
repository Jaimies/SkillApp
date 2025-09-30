package com.theskillapp.skillapp.test

import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.DAYS

fun clockOfEpochDay(epochDay: Long): Clock {
    return clockOfInstant(Instant.EPOCH.plus(epochDay, DAYS))
}

fun clockOfEpochSecond(second: Long): Clock {
    return clockOfInstant(Instant.ofEpochSecond(second))
}

fun clockOfInstant(instant: Instant): Clock {
    return Clock.fixed(instant, ZoneOffset.UTC)
}

fun dateOfEpochSecond(second: Long): ZonedDateTime {
    val instant = Instant.ofEpochSecond(second)
    return ZonedDateTime.ofInstant(instant, ZoneOffset.UTC)
}
