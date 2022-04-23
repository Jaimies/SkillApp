package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.util.toMinutesPartCompat
import java.time.Duration

class DurationPicker : PickerDialog() {
    val duration: Duration
        get() = Duration.ZERO
            .plusHours(firstPicker.value.toLong())
            .plusMinutes(secondPicker.value.toLong() * 5)

    override fun getFirstPickerValues() = Array(24) { index ->
        requireContext().getString(R.string.time_hours, index.toString())
    }

    override fun getSecondPickerValues() = Array(12) { index ->
        requireContext().getString(R.string.time_minutes, (index * 5).toString())
    }

    class Builder : PickerDialog.Builder() {
        override var titleTextResId = R.string.add_time

        override fun createDialog() = DurationPicker()
        override fun build() = super.build() as DurationPicker

        fun setDuration(duration: Duration): Builder {
            if (duration > maxDuration) _setDuration(maxDuration)
            else _setDuration(duration)
            return this
        }

        private fun _setDuration(duration: Duration) {
            setFirstPickerValue(duration.toHours().toInt())
            setSecondPickerValue(duration.toMinutesPartCompat().toInt() / 5)
        }
    }

    companion object {
        private val maxDuration = Duration.ofHours(23).plusMinutes(55)
    }
}
