package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class TimesPicker : IntegerValuePicker<Count>(MeasurementUnit.Times) {
    class Builder : IntegerValuePicker.Builder<Count>(MeasurementUnit.Times) {
        override var titleTextResId = R.string.add_times_record
        override val titleTextInEditModeResId = R.string.change_count
        override val maxValue get() = Count.ofTimes(5_000L)

        override fun createDialog() = TimesPicker()
    }
}
