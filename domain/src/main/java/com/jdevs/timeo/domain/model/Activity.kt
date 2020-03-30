package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Activity(
    val id: String = "",
    val name: String,
    val totalTime: Int,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime,
    val parentActivity: ActivityMinimal?,
    val subActivities: List<ActivityMinimal> = emptyList()
)

data class ActivityMinimal(val id: String, val name: String, val totalTime: Int)

fun Activity.toMinimal() = ActivityMinimal(id, name, totalTime)
