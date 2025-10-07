package com.theskillapp.skillapp.shared.time

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.time.DateProvider
import com.theskillapp.skillapp.shared.util.shortName
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import javax.inject.Inject

class DefaultDateFormatter @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val dateProvider: DateProvider,
) : DateFormatter {
    override fun format(date: LocalDate?): String {
        val currentDate = dateProvider.getCurrentDateWithRespectToDayStartTime()

        if (date == null) return ""
        if (date == currentDate) return context.getString(R.string.today)
        if (date == currentDate.minusDays(1)) return context.getString(R.string.yesterday)
        if(date.year == currentDate.year) return context.getString(R.string.date, date.dayOfWeek.shortName, date.month.shortName, date.dayOfMonth)
        return context.getString(R.string.date_with_year, date.dayOfWeek.shortName, date.month.shortName, date.dayOfMonth, date.year)
    }

    override fun shortFormat(date: LocalDate?): String {
        if (date == null) return ""
        return context.getString(R.string.date_month_and_day, date.month.shortName, date.dayOfMonth)
    }
}
