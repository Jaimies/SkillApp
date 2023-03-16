package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class TimesGoalPicker : GoalPicker<Count>(MeasurementUnit.Times, goalValues) {
    class Builder : GoalPicker.Builder<Count>(MeasurementUnit.Times, goalValues) {
        override fun createDialog() = TimesGoalPicker()
    }

    companion object {
        private val goalValues = arrayOf(
            arrayOf(Count.ZERO),
            Array(2_000, Count::ofTimes),
            Array(10_000, Count::ofTimes),
        )
    }
}
