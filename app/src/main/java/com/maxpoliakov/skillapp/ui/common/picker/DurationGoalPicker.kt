package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import java.time.Duration

class DurationGoalPicker : GoalPicker<Duration>(MeasurementUnit.Millis, goalValues) {
    override fun getWeeklyPickerValue(value: Duration): String {
        return requireContext().getString(R.string.time_hours, value.toHours().toString())
    }

    class Builder : GoalPicker.Builder<Duration>(MeasurementUnit.Millis, goalValues) {
        override fun createDialog() = DurationGoalPicker()
    }

    companion object {
        private val emptyGoalValues = arrayOf(Duration.ZERO)
        private val dailyGoalValues =
            Array(12) { index -> Duration.ofMinutes(index * 5L) } + Array(24) { index -> Duration.ofHours(index + 1L) }
        private val weeklyGoalValues = Array(169) { index -> Duration.ofHours(index.toLong()) }
        private val goalValues = arrayOf(emptyGoalValues, dailyGoalValues, weeklyGoalValues)
    }
}
