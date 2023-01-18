package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiMeasurementUnit

class TimesPicker : IntegerValuePicker(MeasurementUnit.Times) {
    override val unit get() = UiMeasurementUnit.Times

    class Builder : IntegerValuePicker.Builder(MeasurementUnit.Times) {
        override var titleTextResId = R.string.add_times_record
        override val titleTextInEditModeResId = R.string.change_count
        override val maxValue get() = 5_000L

        override fun createDialog() = TimesPicker()
    }
}
