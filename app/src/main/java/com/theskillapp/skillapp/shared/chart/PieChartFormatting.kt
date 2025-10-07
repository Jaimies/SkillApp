@file:JvmName("PieChartFormatter")

package com.theskillapp.skillapp.shared.chart

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.model.UiStatisticInterval
import com.theskillapp.skillapp.shared.time.DateFormatter
import java.time.LocalDate

fun Context.format(dateRange: ClosedRange<LocalDate>?, interval: UiStatisticInterval?, dateFormatter: DateFormatter): String {
    if (interval == null) return ""
    if (dateRange == null) return getString(R.string.all_time)

    return interval.formatter.format(dateRange, dateFormatter, this)
}
