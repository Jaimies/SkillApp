package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration

@AndroidEntryPoint
class DurationPicker : ValuePicker<Duration>(MeasurementUnit.Millis) {
    override val value: Duration
        get() = Duration.ZERO
            .plusHours(firstPicker.value.toLong())
            .plusMinutes(secondPicker.value.toLong())

    override val numberOfFirstPickerValues get() = 24
    override val numberOfSecondPickerValues get() = 60

    override fun formatFirstPickerValue(value: Int): String {
        return requireContext().getString(R.string.time_hours, value.toString())
    }

    override fun formatSecondPickerValue(value: Int): String {
        return requireContext().getString(R.string.time_minutes, value.toString())
    }

    class Builder : ValuePicker.Builder<Duration>(MeasurementUnit.Millis) {
        override var titleTextResId = R.string.add_time
        override val titleTextInEditModeResId = R.string.change_time
        override val maxValue get() = Duration.ofHours(23).plusMinutes(59)

        override fun createDialog() = DurationPicker()

        override fun setValue(value: Duration) {
            setFirstPickerValue(value.toHours().toInt())
            setSecondPickerValue(value.toMinutesPartCompat().toInt())
        }
    }
}
