package com.maxpoliakov.skillapp.ui.common.picker

import android.os.Bundle
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiMeasurementUnit

abstract class IntegerValuePicker(unit: MeasurementUnit<Long>) : ValuePicker<Long>(unit) {
    override val value get() = firstPicker.value.toLong()

    abstract val unit: UiMeasurementUnit

    override fun getFirstPickerValues() = Array(getNumberOfValues()) { index ->
        unit.toLongString(index.toLong(), requireContext())
    }

    private fun getNumberOfValues(): Int {
        return requireArguments().getLong(MAXIMUM_VALUE, 5_000L).toInt()
    }

    override fun getSecondPickerValues() = arrayOf<String>()

    abstract class Builder(unit: MeasurementUnit<Long>) : ValuePicker.Builder<Long>(unit) {
        override var secondPickerEnabled = false

        override fun setValue(value: Long) {
            setFirstPickerValue(value.toInt())
        }

        override fun saveArguments(bundle: Bundle) {
            super.saveArguments(bundle)
            bundle.putInt(MAXIMUM_VALUE, maxValue.toInt() - 1)
        }
    }

    companion object {
        const val MAXIMUM_VALUE = "MAXIMUM_VALUE"
    }
}
