@file:JvmName("PieChartFormatter")

package com.maxpoliakov.skillapp.shared.chart

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import com.maxpoliakov.skillapp.shared.time.DateFormatter
import java.time.LocalDate

fun Context.format(dateRange: ClosedRange<LocalDate>?, interval: UiStatisticInterval?, dateFormatter: DateFormatter): String {
    if (interval == null) return ""
    if (dateRange == null) return getString(R.string.all_time)

    return interval.formatter.format(dateRange, dateFormatter, this)
}
