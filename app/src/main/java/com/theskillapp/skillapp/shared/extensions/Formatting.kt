package com.theskillapp.skillapp.shared.extensions

import android.content.Context
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.Backup
import com.theskillapp.skillapp.model.LoadingState
import com.theskillapp.skillapp.shared.util.dateTimeFormatter
import com.theskillapp.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

fun LoadingState<Backup?>.format(context: Context): String {
    return when (this) {
        is LoadingState.Success -> {
            if (this.value == null) context.getString(R.string.no_backup_found)
            else dateTimeFormatter.format(this.value.creationDate)
        }
        is LoadingState.Loading -> context.getString(R.string.loading_last_backup)
        is LoadingState.Error -> context.getString(R.string.failed_to_load_last_backup_date)
    }
}

@JvmOverloads
fun Duration?.format(context: Context, timeHoursAndMinutesResId: Int = R.string.time_hours_and_minutes): String {
    if (this == null) return ""

    val hours = toHours()
    val minutesPart = toMinutesPartCompat()

    return when {
        hours == 0L -> context.getString(R.string.time_minutes, minutesPart.toString())
        minutesPart == 0L -> context.getString(R.string.time_hours, hours.toString())
        else -> context.getString(timeHoursAndMinutesResId, hours, minutesPart)
    }
}

fun Float.toReadableFloat(): String {
    val string = "%.1f".format(this)
    return if (string.last() == '0') string.dropLast(2) else string
}
