package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Count
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class PageCountGoalPicker : GoalPicker<Count>(MeasurementUnit.Pages, goalValues) {
    class Builder : GoalPicker.Builder<Count>(MeasurementUnit.Pages, goalValues) {
        override fun createDialog() = PageCountGoalPicker()
    }

    companion object {
        private val goalValues = arrayOf(
            arrayOf(Count.ZERO),
            Array(2_000, Count::ofTimes),
            Array(10_000, Count::ofTimes),
        )
    }
}
