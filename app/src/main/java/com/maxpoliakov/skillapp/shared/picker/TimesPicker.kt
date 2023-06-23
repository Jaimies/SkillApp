package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

open class TimesPicker : IntegerValuePicker<Count>(MeasurementUnit.Times) {
    open class Builder : IntegerValuePicker.Builder<Count>(MeasurementUnit.Times) {
        override val maxValue get() = Count.ofTimes(5_000L)

        override fun createDialog() = TimesPicker()
    }
}
