package com.maxpoliakov.skillapp.util.statistics

import com.maxpoliakov.skillapp.domain.model.Statistic
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import java.time.Duration

fun List<Statistic>.getTodayTime(): Duration {
    return this.find { it.date == getCurrentDate() }?.time ?: Duration.ZERO
}
