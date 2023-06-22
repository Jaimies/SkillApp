package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.Distance
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class DistanceGoalPicker : GoalPicker<Distance>(MeasurementUnit.Meters) {
    override fun getValue(pickerValue: Int): Distance {
       return Distance.ofKilometers(pickerValue.toLong())
    }

    class Builder : GoalPicker.Builder<Distance>(MeasurementUnit.Meters) {
        override fun createDialog() = DistanceGoalPicker()
    }
}
