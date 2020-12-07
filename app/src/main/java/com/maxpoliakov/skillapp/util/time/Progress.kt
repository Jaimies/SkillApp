package com.maxpoliakov.skillapp.util.time

import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoUnit.WEEKS

fun getAvgWeekHours(duration: Duration, startedAt: LocalDate): Duration {
    val weeks = WEEKS.between(startedAt, getCurrentDate()) + 1
    return duration.dividedBy(weeks.coerceAtLeast(1L))
}
