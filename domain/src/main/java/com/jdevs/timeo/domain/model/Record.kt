package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val id: Int = 0,
    val name: String,
    val time: Int,
    val activityId: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)
