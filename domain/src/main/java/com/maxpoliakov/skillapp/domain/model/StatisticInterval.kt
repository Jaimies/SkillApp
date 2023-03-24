package com.maxpoliakov.skillapp.domain.model

import com.maxpoliakov.skillapp.shared.util.EPOCH
import com.maxpoliakov.skillapp.shared.util.atStartOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class StatisticInterval {
    Daily {
        override val unit: ChronoUnit get() = ChronoUnit.DAYS
        override val numberOfValues get() = 56

        override fun atStartOfInterval(date: LocalDate) = date
    },
    Weekly {
        override val unit: ChronoUnit get() = ChronoUnit.WEEKS
        override val numberOfValues get() = 21

        override fun atStartOfInterval(date: LocalDate) = date.atStartOfWeek()
    },

    Monthly {
        override val unit: ChronoUnit get() = ChronoUnit.MONTHS
        override val numberOfValues get() = 21

        override fun atStartOfInterval(date: LocalDate) = date.withDayOfMonth(1)
    },

    Yearly {
        override val unit: ChronoUnit get() = ChronoUnit.YEARS
        override val numberOfValues get() = 10

        override fun atStartOfInterval(date: LocalDate) = date.withDayOfYear(1)
    }
    ;

    abstract val unit: ChronoUnit
    abstract val numberOfValues: Int

    abstract fun atStartOfInterval(date: LocalDate): LocalDate

    fun toNumber(date: LocalDate): Long {
        return unit.between(atStartOfInterval(EPOCH), atStartOfInterval(date))
    }

    fun getCurrentDateRange(): ClosedRange<LocalDate> {
        return getDateRangeContaining(LocalDate.now())
    }

    fun toDateRange(number: Long): ClosedRange<LocalDate> {
        return getDateRangeContaining(getStartDate(number))
    }

    private fun getStartDate(number: Long): LocalDate {
        return atStartOfInterval(EPOCH.plus(number, unit))
    }

    fun getDateRangeContaining(date: LocalDate): ClosedRange<LocalDate> {
        val start = atStartOfInterval(date)
        val end = start.plus(1, unit).minusDays(1)
        return start..end
    }
}
