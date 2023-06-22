package com.maxpoliakov.skillapp.shared.picker

import android.os.Bundle
import android.view.View
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.model.UiGoal.Type.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.extensions.disableKeyboardInput

abstract class GoalPicker<T>(
    private val unit: MeasurementUnit<T>,
) : PickerDialog() {
    private val uiUnit get() = unit.mapToUI()

    override val numberOfFirstPickerValues get() = goalTypes.size
    override val numberOfSecondPickerValues get() = 5_000

    override fun formatFirstPickerValue(value: Int): String {
        return requireContext().getString(goalTypes[value]?.goalResId ?: R.string.no_plan)
    }

    override fun formatSecondPickerValue(value: Int): String {
        return uiUnit.toLongString(unit.toLong(getValue(value)), requireContext())
    }

    abstract fun getValue(pickerValue: Int): T

    val goal: Goal?
        get() {
            val type = goalTypes[firstPicker.value] ?: return null
            val value = getValue(secondPicker.value)
            return Goal(unit.toLong(value), type.toDomain())
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstPicker.disableKeyboardInput()
    }

    fun addOnConfirmedListener(callback: (goal: Goal?) -> Unit) {
        addOnPositiveButtonClickListener {
            callback(this.goal)
        }
    }

    abstract class Builder<T>(
        private val unit: MeasurementUnit<T>,
    ) : PickerDialog.Builder<Builder<T>, GoalPicker<T>>() {
        override var titleTextResId = R.string.select_goal

        fun setGoal(goal: Goal?): Builder<T> {
            if (goal == null) {
                setFirstPickerValue(1)
                return this
            }

            runCatching {
                setFirstPickerValue(
                    goalTypes.indexOf(goal.type.mapToUI()).coerceAtLeast(0),
                )
            }

            return this
        }
    }

    companion object {
        private val goalTypes = arrayOf(null, *UiGoal.Type.values())
    }
}
