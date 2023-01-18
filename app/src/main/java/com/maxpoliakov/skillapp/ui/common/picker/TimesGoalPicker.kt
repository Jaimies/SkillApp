package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

class TimesGoalPicker : GoalPicker<Long>(MeasurementUnit.Times) {
    override val goalValues = Companion.goalValues

    class Builder : GoalPicker.Builder<Long>(MeasurementUnit.Times) {
        override val goalValues = Companion.goalValues
        override fun createDialog() = TimesGoalPicker()
    }

    companion object {
        private val goalValues = arrayOf(
            arrayOf(0L),
            Array(2000) { index -> index.toLong() },
            Array(10_000) { index -> index.toLong() }
        )
    }
}
