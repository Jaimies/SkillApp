package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.model.UiMeasurementUnit

class TimesGoalPicker : GoalPicker<Long>() {
    override val goalValues = Companion.goalValues

    override fun toLong(value: Long) = value
    override fun getPickerValue(value: Long): String {
        return UiMeasurementUnit.Times.toLongString(value, requireContext())
    }

    class Builder: GoalPicker.Builder() {
        override fun getSecondPickerValue(firstPickerValue: Int, value: Long): Int {
            return goalValues[firstPickerValue].indexOf(value)
        }

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
