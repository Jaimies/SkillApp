package com.jdevs.timeo.ui.common

import com.github.mikephil.charting.formatter.ValueFormatter
import org.threeten.bp.DayOfWeek
import org.threeten.bp.format.TextStyle
import java.util.Locale

class WeekDayFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return DayOfWeek.of(value.toInt())
            .getDisplayName(TextStyle.SHORT, Locale.getDefault())
            .toUpperCase(Locale.getDefault())
    }
}
