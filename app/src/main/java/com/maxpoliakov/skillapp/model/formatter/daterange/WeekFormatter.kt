package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.time.toShortReadableDate
import com.maxpoliakov.skillapp.shared.util.shortName
import java.time.LocalDate

class WeekFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Weekly

    override fun format(date: LocalDate): String {
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }

    override fun format(range: ClosedRange<LocalDate>, context: Context): String {
        return context.getString(
            R.string.date_range,
            context.toShortReadableDate(range.start),
            context.toShortReadableDate(range.endInclusive),
        )
    }
}
