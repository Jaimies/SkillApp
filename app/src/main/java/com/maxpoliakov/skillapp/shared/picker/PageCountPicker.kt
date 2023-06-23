package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

open class PageCountPicker : IntegerValuePicker<Count>(MeasurementUnit.Pages) {
    open class Builder : IntegerValuePicker.Builder<Count>(MeasurementUnit.Pages) {
        override var titleTextResId = R.string.add_pages
        override val titleTextInEditModeResId get() = R.string.change_number_of_pages
        override val maxValue get() = Count.ofTimes(5_000)

        override fun createDialog() = PageCountPicker()
    }
}
