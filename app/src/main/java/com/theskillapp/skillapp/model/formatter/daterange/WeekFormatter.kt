package com.theskillapp.skillapp.model.formatter.daterange

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.shared.time.DateFormatter
import com.theskillapp.skillapp.shared.util.shortName
import java.time.LocalDate

object WeekFormatter : DateRangeFormatter() {
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
