package com.jdevs.timeo

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DateHandler(
    parentId: Int,
    dayInMonthId: Int,
    weekdayId: Int,
    monthYearId : Int,
    view: View
) {
    private val parent      : LinearLayout = view.findViewById(parentId)
    private val dayInMonth  : TextView     = parent.findViewById(dayInMonthId)
    private val weekday     : TextView     = parent.findViewById(weekdayId)
    private val monthYear   : TextView     = parent.findViewById(monthYearId)


    @Suppress("SpellCheckingInspection")
    fun handleTime() {
        val date         = Calendar.getInstance()
        val day          = date.get(Calendar.DAY_OF_MONTH).toString()
        val weekday      = date.get(Calendar.DAY_OF_WEEK)
        val month        = date.get(Calendar.MONTH).toString()
        val year         = date.get(Calendar.YEAR).toString()

        val monthYear = "$month/$year"

        val dayOfWeek  = SimpleDateFormat("EEEE", Locale.getDefault()).format(weekday)

        this.dayInMonth.text = day
        this.weekday.text = dayOfWeek
        this.monthYear.text = monthYear
    }
}