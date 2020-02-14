package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val id: String = "",
    val name: String,
    val time: Int,
    val activityId: String = "",
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)
