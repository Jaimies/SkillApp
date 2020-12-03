package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate

data class Record(
    val name: String,
    val skillId: Id,
    val time: Duration,
    val id: Id = 0,
    val date: LocalDate = getCurrentDate()
)
