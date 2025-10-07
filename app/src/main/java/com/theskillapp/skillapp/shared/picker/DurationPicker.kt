package com.theskillapp.skillapp.shared.picker

import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.shared.util.toMinutesPartCompat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@AndroidEntryPoint
open class DurationPicker : ValuePicker<Duration>(MeasurementUnit.Millis) {
    override val value: Duration
        get() = Duration.ZERO
            .plusHours(secondPicker.value.toLong())
            .plusMinutes(thirdPicker.value.toLong())

    override fun getPickerValuesForValue(value: Duration): Pair<Int, Int> {
        return value.toHours().toInt() to value.toMinutesPartCompat().toInt()
    }

    override fun formatSecondPickerValue(value: Int): String {
        return requireContext().getString(R.string.time_hours, value.toString())
    }

    override fun formatThirdPickerValue(value: Int): String {
        return requireContext().getString(R.string.time_minutes, value.toString())
    }
}
