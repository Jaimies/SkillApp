package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class DistanceGoalPicker : GoalPicker<Long>(MeasurementUnit.Meters, goalValues) {
    class Builder : GoalPicker.Builder<Long>(MeasurementUnit.Meters, goalValues) {
        override fun createDialog() = DistanceGoalPicker()
    }

    companion object {
        private val goalValues = arrayOf(
            arrayOf(0L),
            Array(2000) { index -> index * 1000L },
            Array(10_000) { index -> index * 1000L }
        )
    }
}
