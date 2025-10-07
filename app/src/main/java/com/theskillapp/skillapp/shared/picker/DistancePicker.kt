package com.theskillapp.skillapp.shared.picker

import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.domain.model.Distance
import com.theskillapp.skillapp.domain.model.MeasurementUnit

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
}
