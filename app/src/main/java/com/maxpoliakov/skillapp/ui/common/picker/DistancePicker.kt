package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import kotlin.math.roundToInt

class DistancePicker : PickerDialog() {
    val distance get() = firstPicker.value * 1000 + secondPicker.value * 100

    override fun getFirstPickerValues() = Array(1000) { index ->
        requireContext().getString(R.string.distance_kilometers, index)
    }

    override fun getSecondPickerValues() = Array(10) { index ->
        requireContext().getString(R.string.distance_meters, index * 100)
    }

    class Builder : PickerDialog.Builder() {
        override var titleTextResId = R.string.add_a_record

        override fun createDialog() = DistancePicker()
        override fun build() = super.build() as DistancePicker

        fun setDistance(count: Long): Builder {
            _setDistance(count.coerceAtMost(999_900))
            return this
        }

        private fun _setDistance(count: Long) {
            setFirstPickerValue((count / 1000f).roundToInt())
            setSecondPickerValue((count % 1000).toInt())
        }
    }
}
