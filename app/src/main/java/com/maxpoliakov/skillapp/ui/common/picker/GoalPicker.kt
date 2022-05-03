package com.maxpoliakov.skillapp.ui.common.picker

import android.content.Context
import android.os.Bundle
import android.view.View
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal

abstract class GoalPicker<T> : PickerDialog() {
    private lateinit var goalStringValues: Array<Array<String>>
    protected abstract val goalValues: Array<Array<T>>

    abstract fun toLong(value: T): Long
    abstract fun getPickerValue(value: T): String

    open fun getWeeklyPickerValue(value: T): String = getPickerValue(value)

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

            return Goal(toLong(values[secondPicker.value]), type)
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        goalStringValues = arrayOf(
            arrayOf(context.getString(R.string.plan_no_time)),
            goalValues[1].map(this::getPickerValue).toTypedArray(),
            goalValues[2].map(this::getWeeklyPickerValue).toTypedArray(),
        )
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

    fun addOnConfirmedListener(callback: (goal: Goal?) -> Unit) {
        addOnPositiveButtonClickListener {
            callback(this.goal)
        }
    }

    abstract class Builder : PickerDialog.Builder() {
        override var titleTextResId = R.string.select_goal

        abstract fun getSecondPickerValue(firstPickerValue: Int, value: Long): Int

        fun setGoal(goal: Goal?): Builder {
            if (goal == null) {
                setFirstPickerValue(1)
                return this
            }

            runCatching {
                val goalValue = goalTypes.indexOf(goal.type).coerceAtLeast(0)
                setFirstPickerValue(goalValue)

                val durationValue = getSecondPickerValue(goalValue, goal.count).coerceAtLeast(0)
                setSecondPickerValue(durationValue)
            }

            return this
        }
    }

    companion object {
        private val goalTypes = arrayOf(null, Goal.Type.Daily, Goal.Type.Weekly)
        private val firstPickerValueResIds = arrayOf(R.string.no_plan, R.string.plan_daily, R.string.plan_weekly)
    }
}
