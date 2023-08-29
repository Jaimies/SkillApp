package com.maxpoliakov.skillapp.model.formatter.daterange

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.StatisticInterval
import com.maxpoliakov.skillapp.shared.time.toReadableDate
import com.maxpoliakov.skillapp.shared.util.getCurrentDate
import com.maxpoliakov.skillapp.shared.util.shortName
import java.time.LocalDate

class DayFormatter : DateRangeFormatter() {
    override val interval = StatisticInterval.Daily
    override val currentDateRangeStringResId = R.string.today

    override fun format(date: LocalDate) : String {
        return "${date.dayOfMonth}\n${date.month.shortName}"
    }

    override fun _format(range: ClosedRange<LocalDate>, context: Context) : String{
        return context.toReadableDate(range.start, getCurrentDate())
    }
}
