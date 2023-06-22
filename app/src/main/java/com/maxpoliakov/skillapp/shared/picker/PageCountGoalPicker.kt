package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class PageCountGoalPicker : GoalPicker<Count>(MeasurementUnit.Pages) {
    override fun getValue(pickerValue: Int): Count {
        return Count.ofTimes(pickerValue.toLong())
    }

    class Builder : GoalPicker.Builder<Count>(MeasurementUnit.Pages) {
        override fun createDialog() = PageCountGoalPicker()
    }
}
