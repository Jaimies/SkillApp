package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R

class TimesPicker: ValuePicker() {
    override val count get() = firstPicker.value.toLong()

    override fun getFirstPickerValues() = Array(5000) { index ->
        requireContext().getString(R.string.count_times, index)
    }

    override fun getSecondPickerValues() = arrayOf<String>()

    class Builder : ValuePicker.Builder() {
        override var titleTextResId = R.string.add_a_record
        override var secondPickerEnabled = false

        override fun createDialog() = TimesPicker()
        override fun build() = super.build() as TimesPicker

        override fun setCount(count: Long): Builder {
            setFirstPickerValue(count.coerceAtMost(5000).toInt())
            return this
        }
    }
}
