package com.jdevs.timeo.ui.common

import com.github.mikephil.charting.formatter.ValueFormatter
import com.jdevs.timeo.shared.time.EPOCH
import org.threeten.bp.format.TextStyle
import java.util.Locale

class WeekDayFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return EPOCH.plusDays(value.toLong())
            .dayOfWeek
            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .toUpperCase(Locale.getDefault())
    }
}
