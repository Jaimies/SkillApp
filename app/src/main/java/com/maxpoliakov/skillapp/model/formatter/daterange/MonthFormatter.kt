package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.util.fullLocalizedName
import com.maxpoliakov.skillapp.shared.util.shortName
import java.time.LocalDate

class MonthFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Monthly

    override fun format(date: LocalDate): String {
        return "${date.month.shortName}\n${date.year % 100}"
    }

    override fun format(range: ClosedRange<LocalDate>, context: Context): String {
        return context.getString(
            R.string.date_month_and_year,
            range.start.month.fullLocalizedName.replaceFirstChar(Char::titlecase),
            range.start.year,
        )
    }
}
