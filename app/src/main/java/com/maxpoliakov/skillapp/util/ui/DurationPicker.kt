package com.maxpoliakov.skillapp.util.ui

import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

class DurationPicker : PickerDialog() {
    val duration: Duration
        get() = Duration.ZERO
            .plusHours(firstPicker.value.toLong())
            .plusMinutes(secondPicker.value.toLong() * 5)

    override val firstPickerValues = Array(24) { index -> "${index}h" }
    override val secondPickerValues = Array(12) { index -> "${index * 5}m" }

    class Builder : PickerDialog.Builder() {
        override fun createDialog() = DurationPicker()

        fun setDuration(duration: Duration): Builder {
            setFirstPickerValue(duration.toHours().toInt())
            setSecondPickerValue(duration.toMinutesPartCompat().toInt() / 5)
            return this
        }

        override fun build() = super.build() as DurationPicker
    }
}
