package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.time.DateFormatter
import com.maxpoliakov.skillapp.shared.util.shortName
import java.time.LocalDate

class WeekFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Weekly
    override val currentDateRangeStringResId = R.string.this_week

    override fun format(date: LocalDate): String {
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }

    override fun _format(range: ClosedRange<LocalDate>, dateFormatter: DateFormatter, context: Context): String {
        return context.getString(
            R.string.date_range,
            dateFormatter.shortFormat(range.start),
            dateFormatter.shortFormat(range.endInclusive),
        )
    }
}
