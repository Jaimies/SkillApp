package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Activity(
    val id: Id,
    val name: String,
    val totalTime: Int,
    val lastWeekTime: Int,
    val timestamp: OffsetDateTime,
    val parentActivity: ActivityMinimal?,
    val subActivities: List<ActivityMinimal>
)

data class ActivityMinimal(val id: Id, val name: String, val totalTime: Int)

fun Activity.toMinimal() = ActivityMinimal(id, name, totalTime)
