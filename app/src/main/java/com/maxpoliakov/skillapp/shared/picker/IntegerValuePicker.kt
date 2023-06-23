package com.maxpoliakov.skillapp.shared.picker

import android.os.Bundle
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

abstract class IntegerValuePicker<T>(
    private val unit: MeasurementUnit<T>,
) : ValuePicker<T>(unit) {

    override val value get() = unit.toType(secondPicker.value.toLong())
    private val uiUnit = unit.mapToUI()

    override val thirdPickerEnabled = false

    override val numberOfSecondPickerValues get() = requireArguments().getInt(MAXIMUM_VALUE, 5_000)
    override val numberOfThirdPickerValues get() = 0

    override fun formatSecondPickerValue(value: Int): String {
        return uiUnit.toLongString(value.toLong(), requireContext())
    }

    override fun formatThirdPickerValue(value: Int) = ""

    abstract class Builder<T : Comparable<T>>(private val unit: MeasurementUnit<T>) : ValuePicker.Builder<T>(unit) {
        override fun setValue(value: T) {
            setSecondPickerValue(unit.toLong(value).toInt())
        }

        override fun saveArguments(bundle: Bundle) {
            super.saveArguments(bundle)
            bundle.putInt(MAXIMUM_VALUE, unit.toLong(maxValue).toInt() - 1)
        }
    }

    companion object {
        const val MAXIMUM_VALUE = "MAXIMUM_VALUE"
    }
}
