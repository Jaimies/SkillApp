package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class DistancePicker : ValuePicker<Long>(MeasurementUnit.Meters) {
    override val value get() = firstPicker.value * 1000L + secondPicker.value * 100L

    override fun getFirstPickerValues() = Array(1000) { index ->
        requireContext().getString(R.string.distance_kilometers, index.toString())
    }

    override fun getSecondPickerValues() = Array(10) { index ->
        requireContext().getString(R.string.distance_meters, index * 100)
    }

    class Builder : ValuePicker.Builder() {
        override var titleTextResId = R.string.add_kilometers_record
        override val titleTextInEditModeResId = R.string.change_distance

        override fun createDialog() = DistancePicker()

        override fun setCount(count: Long): Builder {
            _setDistance(count.coerceAtMost(999_900))
            return this
        }

        private fun _setDistance(value: Long) {
            setFirstPickerValue((value / 1000f).toInt())
            setSecondPickerValue((value % 1000 / 100).toInt())
        }
    }
}
