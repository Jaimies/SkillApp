package com.maxpoliakov.skillapp.shared.picker

import android.os.Bundle
import android.view.View
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.model.UiGoal.Type.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.extensions.disableKeyboardInput

abstract class ValuePicker<T>(private val unit: MeasurementUnit<T>) : PickerDialog() {
    abstract val value: T
    val count get() = unit.toLong(value)

    val goalType get() = goalTypeValues[firstPicker.value]?.toDomain()
    val goal get() = goalType?.let { goalType -> Goal(unit.toLong(value), goalType) }

    override val numberOfFirstPickerValues get() = goalTypeValues.size

    override fun formatFirstPickerValue(value: Int): String {
        return requireContext().getString(goalTypeValues[value]?.goalResId ?: R.string.no_plan)
    }

    fun addOnConfirmedListener(listener: (count: Long) -> Unit) {
        addOnPositiveButtonClickListener { listener(unit.toLong(value)) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstPicker.disableKeyboardInput()
    }

    abstract class Builder<T : Comparable<T>>(
        private val unit: MeasurementUnit<T>,
    ) : PickerDialog.Builder<Builder<T>, ValuePicker<T>>() {
        abstract val titleTextInEditModeResId: Int
        abstract val maxValue: T

        protected abstract fun setValue(value: T)

        fun setEditModeEnabled(isInEditMode: Boolean): Builder<T> {
            setTitleText(if (isInEditMode) titleTextInEditModeResId else titleTextResId)
            return this
        }

        fun setCount(count: Long): Builder<T> {
            setValue(unit.toType(count).coerceAtMost(maxValue))
            return this
        }

        fun setGoalType(type: Goal.Type?): Builder<T> {
            setFirstPickerValue(goalTypeValues.indexOf(type?.mapToUI()).coerceAtLeast(0))
            return this
        }

        fun setGoal(goal: Goal?): Builder<T> {
            setCount(goal?.count ?: 0)
            setGoalType(goal?.type ?: Goal.Type.Daily)

            return this
        }
    }

    companion object {
        val goalTypeValues = arrayOf(null, *UiGoal.Type.values())
    }
}
