package com.maxpoliakov.skillapp.ui.common.picker

import android.content.Context
import android.os.Bundle
import android.view.View
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.model.UiGoal.Type.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.util.ui.setValues

abstract class GoalPicker<T>(
    private val unit: MeasurementUnit<T>,
    private val goalValues: Array<Array<T>>,
) : PickerDialog() {
    private lateinit var goalStringValues: Array<Array<String>>

    private val uiUnit get() = unit.mapToUI()

    open fun getPickerValue(value: T): String {
        return uiUnit.toLongString(unit.toLong(value), requireContext())
    }

    open fun getWeeklyPickerValue(value: T): String = getPickerValue(value)

    override fun getFirstPickerValues() = goalTypes.map { type ->
        requireContext().getString(type?.goalResId ?: R.string.no_plan)
    }.toTypedArray()

    override fun getSecondPickerValues(): Array<String> {
        return goalStringValues[firstPicker.value]
    }

    val goal: Goal?
        get() {
            val type = goalTypes[firstPicker.value] ?: return null
            val values = goalValues[firstPicker.value]

            return Goal(unit.toLong(values[secondPicker.value]), type.toDomain())
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
        secondPicker.setValues(goalStringValues[firstPickerValue])
        firstPicker.setOnValueChangedListener { _, _, newValue ->
            secondPicker.setValues(goalStringValues[newValue])
        }
    }

    override fun restoreStateOfPickers(bundle: Bundle) {
        val firstPickerValue = bundle.getInt(FIRST_PICKER_VALUE, 0)
        firstPicker.value = firstPickerValue
        secondPicker.setValues(goalStringValues[firstPickerValue])
        secondPicker.value = bundle.getInt(SECOND_PICKER_VALUE, 0)
    }

    fun addOnConfirmedListener(callback: (goal: Goal?) -> Unit) {
        addOnPositiveButtonClickListener {
            callback(this.goal)
        }
    }

    abstract class Builder<T>(
        private val unit: MeasurementUnit<T>,
        private val goalValues: Array<Array<T>>,
    ) : PickerDialog.Builder<Builder<T>, GoalPicker<T>>() {
        override var titleTextResId = R.string.select_goal

        fun setGoal(goal: Goal?): Builder<T> {
            if (goal == null) {
                setFirstPickerValue(1)
                return this
            }

            runCatching {
                val goalValue = goalTypes.indexOf(goal.type.mapToUI()).coerceAtLeast(0)
                setFirstPickerValue(goalValue)

                val durationValue = getSecondPickerValue(goalValue, goal.count).coerceAtLeast(0)
                setSecondPickerValue(durationValue)
            }

            return this
        }

        private fun getSecondPickerValue(firstPickerValue: Int, value: Long): Int {
            return goalValues[firstPickerValue].indexOf(unit.toType(value))
        }
    }

    companion object {
        private val goalTypes = arrayOf(null, *UiGoal.Type.values())
    }
}
