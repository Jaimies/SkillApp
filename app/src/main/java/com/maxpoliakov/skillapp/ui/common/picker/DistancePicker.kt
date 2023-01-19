package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Distance
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class DistancePicker : ValuePicker<Distance>(MeasurementUnit.Meters) {
    override val value
        get() = Distance
            .ofKilometers(firstPicker.value.toLong())
            .plusMeters(secondPicker.value * 100L)

    override fun getFirstPickerValues() = Array(1000) { index ->
        requireContext().getString(R.string.distance_kilometers, index.toString())
    }

    override fun getSecondPickerValues() = Array(10) { index ->
        requireContext().getString(R.string.distance_meters, index * 100)
    }

    class Builder : ValuePicker.Builder<Distance>(MeasurementUnit.Meters) {
        override var titleTextResId = R.string.add_kilometers_record
        override val titleTextInEditModeResId = R.string.change_distance
        override val maxValue get() = Distance.ofKilometers(999).plusMeters(900)

        override fun createDialog() = DistancePicker()

        override fun setValue(value: Distance) {
            setFirstPickerValue(value.toKilometers().toInt())
            setSecondPickerValue((value.toMetersPart() / 100).toInt())
        }
    }
}
