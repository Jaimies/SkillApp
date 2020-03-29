package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Activity(
    val id: String = "",
    val name: String,
    val totalTime: Int,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime,
    val parentActivityName: String = "",
    val parentActivityId: String = "",
    val subActivities: List<ActivityMinimal> = emptyList()
)

data class ActivityMinimal(val name: String, val totalTime: Int)