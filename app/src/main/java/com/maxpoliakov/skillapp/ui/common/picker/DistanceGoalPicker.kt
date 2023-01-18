package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.model.UiMeasurementUnit

class DistanceGoalPicker : GoalPicker<Long>(UiMeasurementUnit.Meters) {
    override val goalValues = Companion.goalValues

    override fun toLong(value: Long) = value

    class Builder : GoalPicker.Builder() {
        override fun getSecondPickerValue(firstPickerValue: Int, value: Long): Int {
            return goalValues[firstPickerValue].indexOf(value)
        }

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
