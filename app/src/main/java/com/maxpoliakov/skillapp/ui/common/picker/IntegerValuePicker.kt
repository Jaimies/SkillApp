package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.model.UiMeasurementUnit

abstract class IntegerValuePicker : ValuePicker() {
    override val count get() = firstPicker.value.toLong()

    abstract val unit: UiMeasurementUnit

    override fun getFirstPickerValues() = Array(5000) { index ->
        unit.toLongString(index.toLong(), requireContext())
    }

    override fun getSecondPickerValues() = arrayOf<String>()

    abstract class Builder : ValuePicker.Builder() {
        open val maximumValue: Long = 5_000L

        override var secondPickerEnabled = false

        override fun setCount(count: Long): Builder {
            setFirstPickerValue(count.coerceAtMost(maximumValue).toInt())
            return this
        }
    }
}
