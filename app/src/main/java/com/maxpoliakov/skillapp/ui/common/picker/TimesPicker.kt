package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.UiMeasurementUnit

class TimesPicker: IntegerValuePicker() {
    override val unit get() = UiMeasurementUnit.Times

    class Builder : IntegerValuePicker.Builder() {
        override var titleTextResId = R.string.add_times_record
        override val titleTextInEditModeResId = R.string.change_count

        override fun createDialog() = TimesPicker()
    }
}
