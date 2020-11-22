package com.jdevs.timeo.domain.model

import java.time.Duration
import java.time.OffsetDateTime

data class Activity(
    val name: String,
    val totalTime: Duration,
    val parentActivity: ActivityMinimal?,
    val subActivities: List<ActivityMinimal> = listOf(),
    val lastWeekTime: Duration = Duration.ZERO,
    val id: Id = 0,
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)

data class ActivityMinimal(
    val id: Id,
    val name: String,
    val totalTime: Duration
)

fun Activity.toMinimal() = ActivityMinimal(id, name, totalTime)
