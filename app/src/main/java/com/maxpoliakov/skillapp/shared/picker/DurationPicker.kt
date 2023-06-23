package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@AndroidEntryPoint
open class DurationPicker : ValuePicker<Duration>(MeasurementUnit.Millis) {
    override val value: Duration
        get() = Duration.ZERO
            .plusHours(secondPicker.value.toLong())
            .plusMinutes(thirdPicker.value.toLong())

    override val numberOfSecondPickerValues get() = 24
    override val numberOfThirdPickerValues get() = 60

    override fun formatSecondPickerValue(value: Int): String {
        return requireContext().getString(R.string.time_hours, value.toString())
    }

    override fun formatThirdPickerValue(value: Int): String {
        return requireContext().getString(R.string.time_minutes, value.toString())
    }

    open class Builder : ValuePicker.Builder<Duration>(MeasurementUnit.Millis) {
        override val maxValue get() = Duration.ofHours(23).plusMinutes(59)

        override fun createDialog() = DurationPicker()

        override fun setValue(value: Duration) {
            setSecondPickerValue(value.toHours().toInt())
            setThirdPickerValue(value.toMinutesPartCompat().toInt())
        }
    }
}
