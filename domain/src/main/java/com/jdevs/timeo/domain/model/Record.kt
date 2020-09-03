package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val id: Int = 0,
    val activity: ActivityMinimal,
    val time: Int,
    val creationDate: OffsetDateTime = OffsetDateTime.now()
)
