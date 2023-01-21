package com.maxpoliakov.skillapp.ui.common.picker

import android.os.Bundle
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI

abstract class IntegerValuePicker<T>(
    private val unit: MeasurementUnit<T>,
) : ValuePicker<T>(unit) {

    override val value get() = unit.toType(firstPicker.value.toLong())
    private val uiUnit = unit.mapToUI()

    override fun getFirstPickerValues() = Array(getNumberOfValues()) { index ->
        uiUnit.toLongString(index.toLong(), requireContext())
    }

    private fun getNumberOfValues(): Int {
        return requireArguments().getLong(MAXIMUM_VALUE, 5_000L).toInt()
    }

    override fun getSecondPickerValues() = arrayOf<String>()

    abstract class Builder<T : Comparable<T>>(private val unit: MeasurementUnit<T>) : ValuePicker.Builder<T>(unit) {
        override var secondPickerEnabled = false

        override fun setValue(value: T) {
            setFirstPickerValue(unit.toLong(value).toInt())
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
