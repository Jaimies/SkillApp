package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.time.DateFormatter
import java.time.LocalDate

object YearFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Yearly
    override val currentDateRangeStringResId = R.string.this_year

    override fun format(date: LocalDate): String {
        return date.year.toString()
    }

    override fun _format(range: ClosedRange<LocalDate>, dateFormatter: DateFormatter, context: Context): String {
        return context.getString(R.string.date_year_only, range.start.year)
    }
}
