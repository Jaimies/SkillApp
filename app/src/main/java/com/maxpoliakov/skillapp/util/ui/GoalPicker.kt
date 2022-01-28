package com.maxpoliakov.skillapp.util.ui

import android.os.Bundle
import android.view.View
import com.maxpoliakov.skillapp.domain.model.Goal
import java.time.Duration

class GoalPicker : PickerDialog() {
    override val firstPickerValues: Array<String>
        get() = arrayOf("No plan", "Daily", "Weekly")
    override val secondPickerValues: Array<String>
        get() = goalStringValues[firstPicker.value]

    val goal: Goal?
        get() {
            val type = goalTypes[firstPicker.value] ?: return null
            val values = goalValues[firstPicker.value]

            return Goal(values[secondPicker.value], type)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstPickerValue = savedInstanceState?.getInt(FIRST_PICKER_VALUE, 0) ?: 0
        secondPicker.refreshByNewDisplayedValues(goalStringValues[firstPickerValue])
        firstPicker.setOnValueChangeListenerInScrolling { _, _, newValue ->
            secondPicker.refreshByNewDisplayedValues(goalStringValues[newValue])
        }
    }


    class Builder : PickerDialog.Builder() {
        var goal: Goal? = null

        override fun createDialog() = GoalPicker()
        override fun build() = super.build() as GoalPicker
    }

    companion object {
        private val goalTypes = arrayOf(null, Goal.Type.Daily, Goal.Type.Weekly)
        private val dailyGoalValues =
            Array(4) { index -> Duration.ofMinutes(index * 15L) } + Array(24) { index -> Duration.ofHours(index + 1L) }
        private val weeklyGoalValues = Array(169) { index -> Duration.ofHours(index.toLong()) }
        private val goalValues = arrayOf(arrayOf(Duration.ZERO), dailyGoalValues, weeklyGoalValues)
        private val dailyGoalStringValues =
            Array(4) { index -> "${index * 15}m" } + Array(24) { index -> "${index + 1}h" }
        private val weeklyGoalStringValues = Array(169) { index -> "${index}h" }
        private val goalStringValues = arrayOf(arrayOf("--"), dailyGoalStringValues, weeklyGoalStringValues)
    }
}
