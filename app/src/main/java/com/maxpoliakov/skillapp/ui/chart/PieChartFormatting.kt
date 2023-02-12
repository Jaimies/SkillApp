@file:JvmName("PieChartFormatter")

package com.maxpoliakov.skillapp.ui.chart

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.util.time.toReadableDate
import java.time.LocalDate

fun Context.format(dateRange: ClosedRange<LocalDate>?): String {
    if (dateRange == null) return getString(R.string.all_time)

    if (dateRange.start == dateRange.endInclusive) {
        return toReadableDate(dateRange.start)
    }

    return getString(
        R.string.date_range,
        toReadableDate(dateRange.start),
        toReadableDate(dateRange.endInclusive),
    )
}
