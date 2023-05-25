package com.maxpoliakov.skillapp.domain.model

import java.time.ZonedDateTime

data class Timer(
    val skillId: Int,
    val startTime: ZonedDateTime,
)
