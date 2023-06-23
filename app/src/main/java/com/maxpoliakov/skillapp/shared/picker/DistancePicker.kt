package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Distance
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

open class DistancePicker : ValuePicker<Distance>(MeasurementUnit.Meters) {
    override val value
        get() = Distance
            .ofKilometers(secondPicker.value.toLong())
            .plusMeters(thirdPicker.value * 100L)

    override val numberOfSecondPickerValues get() = 1000
    override val numberOfThirdPickerValues get() = 10

    override fun formatSecondPickerValue(value: Int): String {
        return requireContext().getString(R.string.distance_kilometers, value.toString())
    }

    override fun formatThirdPickerValue(value: Int): String {
        return requireContext().getString(R.string.distance_meters, value * 100)
    }

    open class Builder : ValuePicker.Builder<Distance>(MeasurementUnit.Meters) {
        override var titleTextResId = R.string.add_kilometers_record
        override val titleTextInEditModeResId = R.string.change_distance
        override val maxValue get() = Distance.ofKilometers(999).plusMeters(900)

        override fun createDialog() = DistancePicker()

        override fun setValue(value: Distance) {
            setSecondPickerValue(value.toKilometers().toInt())
            setThirdPickerValue((value.toMetersPart() / 100).toInt())
        }
    }
}
