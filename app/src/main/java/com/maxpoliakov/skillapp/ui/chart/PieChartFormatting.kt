@file:JvmName("PieChartFormatter")

package com.maxpoliakov.skillapp.ui.chart

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.UiStatisticInterval
import java.time.LocalDate

fun Context.format(dateRange: ClosedRange<LocalDate>?, interval: UiStatisticInterval?): String {
    if (interval == null) return ""
    if (dateRange == null) return getString(R.string.all_time)

    return interval.formatDateRange(dateRange, this)
}
