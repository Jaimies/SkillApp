package com.maxpoliakov.skillapp.ui.common.picker

import android.content.Context
import com.maxpoliakov.skillapp.R
import kotlin.math.roundToInt

class DistancePicker : PickerDialog() {
    val distance get() = firstPicker.value * 1000 + secondPicker.value * 100

    override lateinit var firstPickerValues: Array<String>
    override lateinit var secondPickerValues: Array<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        firstPickerValues = Array(1000) {index ->
            context.getString(R.string.distance_kilometers, index)
        }

        secondPickerValues = Array(10) {index ->
            context.getString(R.string.distance_meters, index * 100)
        }
    }

    class Builder : PickerDialog.Builder() {
        override fun createDialog() = DistancePicker()
        override fun build() = super.build() as DistancePicker

        init {
            setTitleText(R.string.add_a_record)
        }

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
