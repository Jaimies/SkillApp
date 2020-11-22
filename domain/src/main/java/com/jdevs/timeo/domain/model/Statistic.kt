package com.jdevs.timeo.domain.model

import java.time.Duration
import java.time.LocalDate
import java.time.YearMonth
import org.threeten.extra.YearWeek

interface Statistic {
    val time: Duration
}

data class DayStatistic(val date: LocalDate, override val time: Duration): Statistic
data class WeekStatistic(val week: YearWeek, override val time: Duration): Statistic
data class MonthStatistic(val month: YearMonth, override val time: Duration): Statistic
