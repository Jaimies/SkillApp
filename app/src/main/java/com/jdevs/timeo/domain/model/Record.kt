package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val id: Int = 0,
    val documentId: String = "",
    val name: String,
    val time: Long,
    val activityId: String = "",
    val roomActivityId: Int = 0,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)
