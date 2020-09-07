package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Project(
    val id: Id,
    val name: String,
    val activity: ActivityMinimal?,
    val totalTime: Int,
    val lastWeekTime: Int,
    val timestamp: OffsetDateTime
)
