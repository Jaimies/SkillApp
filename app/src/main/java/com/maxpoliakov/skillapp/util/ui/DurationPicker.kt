package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

class DurationPicker : PickerDialog() {
    val duration: Duration
        get() = Duration.ZERO
            .plusHours(firstPicker.value.toLong())
            .plusMinutes(secondPicker.value.toLong() * 5)

    override lateinit var firstPickerValues: Array<String>
    override lateinit var secondPickerValues: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        firstPickerValues = Array(24) { index ->
            context.getString(R.string.hours, index.toString())
        }

        secondPickerValues = Array(12) { index ->
            context.getString(R.string.minutes, (index * 5).toString())
        }
    }

    class Builder : PickerDialog.Builder() {
        override fun createDialog() = DurationPicker()

        init {
            setTitleText(R.string.add_time)
        }

        fun setDuration(duration: Duration): Builder {
            setFirstPickerValue(duration.toHours().toInt())
            setSecondPickerValue(duration.toMinutesPartCompat().toInt() / 5)
            return this
        }

        override fun build() = super.build() as DurationPicker
    }
}
