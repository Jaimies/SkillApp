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
        open val numberOfValues = 5_000
        private val maxValue get() = numberOfValues - 1

        override var secondPickerEnabled = false

        override fun setValue(value: Long) {
            setFirstPickerValue(value.toInt().coerceAtMost(maxValue))
        }

        override fun saveArguments(bundle: Bundle) {
            super.saveArguments(bundle)
            bundle.putInt(MAXIMUM_VALUE, numberOfValues)
        }
    }

    companion object {
        const val MAXIMUM_VALUE = "MAXIMUM_VALUE"
    }
}
