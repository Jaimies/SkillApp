package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate

data class Skill(
    val name: String,
    val totalTime: Duration,
    val initialTime: Duration,
    val lastWeekTime: Duration = Duration.ZERO,
    val id: Id = 0,
    val date: LocalDate = getCurrentDate()
) {
    val recordedTime get() = totalTime - initialTime
}
