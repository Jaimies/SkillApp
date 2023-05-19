package com.maxpoliakov.skillapp.domain.model

import java.time.ZonedDateTime

data class Timer(
    val startTime: ZonedDateTime,
    val skillId: Int,
    val groupId: Int,
)
