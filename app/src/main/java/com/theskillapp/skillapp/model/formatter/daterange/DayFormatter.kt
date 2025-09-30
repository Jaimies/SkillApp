package com.theskillapp.skillapp.model.formatter.daterange

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.StatisticInterval
import com.theskillapp.skillapp.shared.time.DateFormatter
import com.theskillapp.skillapp.shared.util.shortName
import java.time.LocalDate

object DayFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Daily
    override val currentDateRangeStringResId = R.string.today

    override fun format(date: LocalDate) : String {
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }

    override fun _format(range: ClosedRange<LocalDate>, dateFormatter: DateFormatter, context: Context) : String{
        return dateFormatter.format(range.start)
    }
}
