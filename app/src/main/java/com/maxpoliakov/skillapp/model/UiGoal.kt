package com.maxpoliakov.skillapp.model

import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

data class UiGoal(
    val goal: Goal,
    val unit: UiMeasurementUnit,
) {
    companion object {
        fun from(goal: Goal, unit: MeasurementUnit): UiGoal {
            return UiGoal(goal, UiMeasurementUnit.from(unit))
        }

        fun Goal.mapToUI(unit: MeasurementUnit) = from(this, unit)
    }
}
