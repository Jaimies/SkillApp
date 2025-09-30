package com.theskillapp.skillapp.domain.model

import java.time.Duration
import java.time.LocalDate

data class Statistic(
    val date: LocalDate,
    val count: Long,
)
