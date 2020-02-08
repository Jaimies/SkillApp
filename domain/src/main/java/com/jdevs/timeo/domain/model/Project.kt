package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Project(
    val id: Int = 0,
    val documentId: String = "",
    val name: String,
    val totalTime: Long,
    val lastWeekTime: Int,
    val creationDate: OffsetDateTime
)
