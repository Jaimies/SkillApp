package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Distance
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class DistanceGoalPicker : GoalPicker<Distance>(MeasurementUnit.Meters, goalValues) {
    class Builder : GoalPicker.Builder<Distance>(MeasurementUnit.Meters, goalValues) {
        override fun createDialog() = DistanceGoalPicker()
    }

    companion object {
        private val goalValues = arrayOf(
            arrayOf(Distance.ZERO),
            Array(2_000, Distance::ofKilometers),
            Array(10_000, Distance::ofKilometers)
        )
    }
}
