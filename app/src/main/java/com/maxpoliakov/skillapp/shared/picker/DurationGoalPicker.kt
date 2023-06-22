package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import java.time.Duration

class DurationGoalPicker : GoalPicker<Duration>(MeasurementUnit.Millis) {
    override fun getValue(pickerValue: Int): Duration {
        return Duration.ofHours(pickerValue.toLong())
    }

    class Builder : GoalPicker.Builder<Duration>(MeasurementUnit.Millis) {
        override fun createDialog() = DurationGoalPicker()
    }
}
