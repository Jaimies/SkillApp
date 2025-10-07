package com.theskillapp.skillapp.model.formatter.daterange

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.shared.time.DateFormatter
import com.theskillapp.skillapp.shared.util.fullLocalizedName
import com.theskillapp.skillapp.shared.util.shortName
import java.time.LocalDate

object MonthFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Monthly
    override val currentDateRangeStringResId = R.string.this_month

    override fun format(date: LocalDate): String {
        return "${date.month.shortName}\n${date.year % 100}"
    }

    override fun _format(range: ClosedRange<LocalDate>, dateFormatter: DateFormatter, context: Context): String {
        return context.getString(
            R.string.date_month_and_year,
            range.start.month.fullLocalizedName.replaceFirstChar(Char::titlecase),
            range.start.year,
        )
    }
}
