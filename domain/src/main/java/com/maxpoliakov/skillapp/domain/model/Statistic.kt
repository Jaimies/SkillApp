package com.maxpoliakov.skillapp.domain.model

import java.time.Duration
import java.time.LocalDate

data class Statistic(
    val date: LocalDate,
    val time: Duration
)
