package com.jdevs.timeo.domain.model

import java.time.OffsetDateTime

data class Activity(
    val name: String,
    val totalTime: Int,
    val parentActivity: ActivityMinimal?,
    val subActivities: List<ActivityMinimal> = listOf(),
    val lastWeekTime: Int = 0,
    val id: Id = 0,
    val timestamp: OffsetDateTime = OffsetDateTime.now()
)

data class ActivityMinimal(val id: Id, val name: String, val totalTime: Int)

fun Activity.toMinimal() = ActivityMinimal(id, name, totalTime)
