package com.jdevs.timeo.domain.model

import org.threeten.bp.OffsetDateTime

data class Record(
    val id: Int,
    val activity: ActivityMinimal,
    val time: Int,
    val timestamp: OffsetDateTime
)
