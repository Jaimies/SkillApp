@file:JvmName("RecordFormatter")

package com.theskillapp.skillapp.shared.history

import android.content.Context
import android.text.style.ForegroundColorSpan
import androidx.core.text.buildSpannedString
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.model.HistoryUiModel
import com.theskillapp.skillapp.shared.util.toMinutesPartCompat
import com.theskillapp.skillapp.shared.util.toSecondsPartCompat
import com.theskillapp.skillapp.shared.extensions.getColorAttributeValueWithAlpha
import com.theskillapp.skillapp.shared.extensions.setSpanForWholeString
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun Context.getFormattedTime(record: HistoryUiModel.Record?): CharSequence? {
    if (record == null) return null
    if (record.dateTimeRange == null) return format(Duration.ofMillis(record.count))

    return buildSpannedString {
        append("${format(record.dateTimeRange.start)} – ")
        append("${format(record.dateTimeRange.endInclusive)} | ")
        val color = getColorAttributeValueWithAlpha(android.R.attr.textColorPrimary, 120)
        setSpanForWholeString(ForegroundColorSpan(color))
        append(format(Duration.ofMillis(record.count)))
    }
}

private fun Context.format(duration: Duration): String {
    val hours = duration.toHours()

    if (hours == 0L) return getString(
        R.string.duration_format_without_hours,
        duration.toMinutes().toPaddedString(),
        duration.toSecondsPartCompat().toPaddedString()
    )

    return getString(
        R.string.duration_format,
        duration.toHours().toPaddedString(),
        duration.toMinutesPartCompat().toPaddedString(),
        duration.toSecondsPartCompat().toPaddedString(),
    )
}

private fun Long.toPaddedString(): String {
    return this.toString().padStart(2, '0')
}

private fun format(time: LocalTime): String {
    return time.format(DateTimeFormatter.ofPattern("HH:mm"))
}
