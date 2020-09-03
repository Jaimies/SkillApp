package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Activity(
    val id: Int = 0,
    val name: String,
    val totalTime: Int,
    val lastWeekTime: Int = 0,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val parentActivity: ActivityMinimal?,
    val subActivities: List<ActivityMinimal> = emptyList()
)

data class ActivityMinimal(val id: Int, val name: String, val totalTime: Int)

fun Activity.toMinimal() = ActivityMinimal(id, name, totalTime)
