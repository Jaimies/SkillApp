package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Project(
    val id: String = "",
    val name: String,
    val description: String,
    val totalTime: Int,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
)
