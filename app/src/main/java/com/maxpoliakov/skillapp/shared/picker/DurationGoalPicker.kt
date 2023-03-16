package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import java.time.Duration

class DurationGoalPicker : GoalPicker<Duration>(MeasurementUnit.Millis, goalValues) {
    class Builder : GoalPicker.Builder<Duration>(MeasurementUnit.Millis, goalValues) {
        override fun createDialog() = DurationGoalPicker()
    }

    companion object {
        private val emptyGoalValues = arrayOf(Duration.ZERO)
        private val dailyGoalValues =
            Array(12) { index -> Duration.ofMinutes(index * 5L) } + Array(24) { index -> Duration.ofHours(index + 1L) }
        private val weeklyGoalValues =
            Array(4) { index -> Duration.ofMinutes(index * 15L) } + Array(168) { index -> Duration.ofHours(index + 1L) }
        private val goalValues = arrayOf(emptyGoalValues, dailyGoalValues, weeklyGoalValues)
    }
}
