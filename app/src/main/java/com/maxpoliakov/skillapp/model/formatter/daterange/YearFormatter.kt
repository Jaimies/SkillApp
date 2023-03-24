package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import java.time.LocalDate

class YearFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Yearly
    override val currentDateRangeStringResId = R.string.this_year

    override fun format(date: LocalDate): String {
        return date.year.toString()
    }

    override fun _format(range: ClosedRange<LocalDate>, context: Context): String {
        return context.getString(R.string.date_year_only, range.start.year)
    }
}
