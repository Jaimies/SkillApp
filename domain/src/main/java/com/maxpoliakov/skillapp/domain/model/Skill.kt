package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration
import java.time.LocalDate

data class TimeTarget(val duration: Duration, val interval: Interval) {
    enum class Interval { Daily, Weekly }
}

data class Skill(
    val name: String,
    val totalTime: Duration,
    val initialTime: Duration,
    val lastWeekTime: Duration = Duration.ZERO,
    val id: Id = 0,
    val date: LocalDate = getCurrentDate(),
    val groupId: Int = -1,
    val target: TimeTarget?,
    override val order: Int = -1,
) : Orderable
