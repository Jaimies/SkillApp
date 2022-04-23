package com.maxpoliakov.skillapp.ui.common.picker

import android.content.Context
import android.os.Bundle
import android.view.View
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.util.ui.format
import java.time.Duration

class GoalPicker : PickerDialog() {
    private lateinit var goalStringValues: Array<Array<String>>

    override fun getFirstPickerValues() = firstPickerValueResIds.map { resId ->
        requireContext().getString(resId)
    }.toTypedArray()

    override fun getSecondPickerValues(): Array<String> {
        return goalStringValues[firstPicker.value]
    }

    val goal: Goal?
        get() {
            val type = goalTypes[firstPicker.value] ?: return null
            val values = goalValues[firstPicker.value]

            return Goal(values[secondPicker.value].toMillis(), type)
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val dailyGoalStringValues = dailyGoalValues.map { it.format(context) }.toTypedArray()
        val weeklyGoalStringValues = weeklyGoalValues.map { it.format(context) }.toTypedArray()
        val noPlanGoalValues = arrayOf(context.getString(R.string.plan_no_time))
        goalStringValues = arrayOf(noPlanGoalValues, dailyGoalStringValues, weeklyGoalStringValues)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstPickerValue = savedInstanceState?.getInt(FIRST_PICKER_VALUE, 0) ?: 0
        secondPicker.refreshByNewDisplayedValues(goalStringValues[firstPickerValue])
        firstPicker.setOnValueChangeListenerInScrolling { _, _, newValue ->
            secondPicker.refreshByNewDisplayedValues(goalStringValues[newValue])
        }
    }

    override fun restoreStateOfPickers(bundle: Bundle) {
        val firstPickerValue = bundle.getInt(FIRST_PICKER_VALUE, 0)
        firstPicker.value = firstPickerValue
        secondPicker.refreshByNewDisplayedValues(goalStringValues[firstPickerValue])
        secondPicker.value = bundle.getInt(SECOND_PICKER_VALUE, 0)
    }

    class Builder : PickerDialog.Builder() {
        override var titleTextResId = R.string.select_goal

        override fun createDialog() = GoalPicker()
        override fun build() = super.build() as GoalPicker

        fun setGoal(goal: Goal?): Builder {
            if (goal == null) {
                setFirstPickerValue(1)
                return this
            }

            runCatching {
                val goalValue = goalTypes.indexOf(goal.type).coerceAtLeast(0)
                setFirstPickerValue(goalValue)

                val durationValue = goalValues[goalValue].indexOf(Duration.ofMillis(goal.count)).coerceAtLeast(0)
                setSecondPickerValue(durationValue)
            }

            return this
        }
    }

    companion object {
        private val goalTypes = arrayOf(null, Goal.Type.Daily, Goal.Type.Weekly)
        private val dailyGoalValues =
            Array(4) { index -> Duration.ofMinutes(index * 15L) } + Array(24) { index -> Duration.ofHours(index + 1L) }
        private val weeklyGoalValues = Array(169) { index -> Duration.ofHours(index.toLong()) }
        private val goalValues = arrayOf(arrayOf(Duration.ZERO), dailyGoalValues, weeklyGoalValues)
        private val firstPickerValueResIds = arrayOf(R.string.no_plan, R.string.plan_daily, R.string.plan_weekly)
    }
}
