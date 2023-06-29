package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Distance
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

open class DistancePicker : ValuePicker<Distance>(MeasurementUnit.Meters) {
    override val value
        get() = Distance
            .ofKilometers(secondPicker.value.toLong())
            .plusMeters(thirdPicker.value * 100L)

    override fun getPickerValuesForValue(value: Distance): Pair<Int, Int> {
        return value.toKilometers().toInt() to (value.toMetersPart() / 100).toInt()
    }

    override fun formatSecondPickerValue(value: Int): String {
        return requireContext().getString(R.string.distance_kilometers, value.toString())
    }

    override fun formatThirdPickerValue(value: Int): String {
        return requireContext().getString(R.string.distance_meters, value * 100)
    }

    open class Builder : ValuePicker.Builder<Distance>(MeasurementUnit.Meters) {
        override fun createDialog() = DistancePicker()

        override fun setValue(value: Distance) {
            setSecondPickerValue(value.toKilometers().toInt())
            setThirdPickerValue((value.toMetersPart() / 100).toInt())
        }
    }
}
