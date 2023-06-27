package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

abstract class IntegerValuePicker<T>(unit: MeasurementUnit<T>) : ValuePicker<T>(unit) {
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

    abstract class Builder<T : Comparable<T>>(private val unit: MeasurementUnit<T>) : ValuePicker.Builder<T>(unit) {
        override fun setValue(value: T) {
            setSecondPickerValue(unit.toLong(value).toInt())
        }
    }
}
