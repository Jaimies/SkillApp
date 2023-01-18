package com.maxpoliakov.skillapp.ui.common.picker

import android.os.Bundle
import com.maxpoliakov.skillapp.model.UiMeasurementUnit

abstract class IntegerValuePicker : ValuePicker() {
    override val count get() = firstPicker.value.toLong()

    abstract val unit: UiMeasurementUnit

    override fun getFirstPickerValues() = Array(getNumberOfValues()) { index ->
        unit.toLongString(index.toLong(), requireContext())
    }

    private fun getNumberOfValues(): Int {
        return requireArguments().getLong(MAXIMUM_VALUE, 5_000L).toInt()
    }

    override fun getSecondPickerValues() = arrayOf<String>()

    abstract class Builder : ValuePicker.Builder() {
        open val numberOfValues = 5_000
        private val maxValue get() = numberOfValues - 1

        override var secondPickerEnabled = false

        override fun setCount(count: Long): Builder {
            setFirstPickerValue(count.toInt().coerceAtMost(maxValue))
            return this
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
