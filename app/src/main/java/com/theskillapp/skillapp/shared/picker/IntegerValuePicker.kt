package com.theskillapp.skillapp.shared.picker

import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.model.UiMeasurementUnit.Companion.mapToUI

open class IntegerValuePicker<T>(unit: MeasurementUnit<T>) : ValuePicker<T>(unit) {
    override val value get() = unit.toType(secondPicker.value.toLong())
    private val uiUnit = unit.mapToUI()

    override val thirdPickerEnabled = false

    override fun getPickerValuesForValue(value: T): Pair<Int, Int> {
        return unit.toLong(value).toInt() to 0
    }

    override fun formatSecondPickerValue(value: Int): String {
        return uiUnit.toLongString(value.toLong(), requireContext())
    }

    override fun formatThirdPickerValue(value: Int) = ""
}
