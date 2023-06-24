package com.maxpoliakov.skillapp.shared.picker

import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Goal
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.model.UiGoal
import com.maxpoliakov.skillapp.model.UiGoal.Type.Companion.mapToUI
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.model.UiMeasurementUnit.Companion.mapToUI
import com.maxpoliakov.skillapp.shared.extensions.disableKeyboardInput
import com.maxpoliakov.skillapp.shared.extensions.enableKeyboardInput
import com.maxpoliakov.skillapp.shared.extensions.setValues
import com.maxpoliakov.skillapp.shared.hardware.hideKeyboard

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
        if (firstPickerEnabled) setupFirstPicker()
    }

    private fun setupFirstPicker() {
        firstPicker.disableKeyboardInput()

        firstPicker.setOnValueChangedListener { _, _, newVal ->
            configureGoalValuePickers(goalTypeValues[newVal])
        }
    }

    private fun configureGoalValuePickers(goalType: UiGoal.Type?) {
        if (goalType == null) {
            secondPicker.setBlankValues()
            thirdPicker.setBlankValues()
        } else {
            secondPicker.enableKeyboardInput()
            thirdPicker.enableKeyboardInput()
            configureSecondPickerValues()
            configureThirdPickerValues()
        }
    }

    private fun NumberPicker.setBlankValues() {
        setValues(1) { context.getString(R.string.plan_no_time) }
        disableKeyboardInput()
        hideKeyboard()
    }

    abstract class Builder<T : Comparable<T>>(
        private val unit: MeasurementUnit<T>,
    ) : PickerDialog.Builder<Builder<T>, ValuePicker<T>>() {
        abstract val maxValue: T

        private var mode = Mode.ValuePicker
        private var isInEditMode = false

        protected abstract fun setValue(value: T)

        fun setEditModeEnabled(isInEditMode: Boolean): Builder<T> {
            this.isInEditMode = isInEditMode
            return this
        }

        fun setCount(count: Long): Builder<T> {
            setValue(unit.toType(count).coerceAtMost(maxValue))
            return this
        }

        fun setMode(mode: Mode): Builder<T> {
            this.mode = mode
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

        override fun build(): ValuePicker<T> {
            setTitleText(
                if (isInEditMode) mode.getEditModeTitleTextResId(unit.mapToUI())
                else mode.getTitleTextResId(unit.mapToUI())
            )

            setEnableFirstPicker(mode.goalPickerEnabled)

            return super.build()
        }
    }

    enum class Mode {
        ValuePicker {
            override fun getTitleTextResId(unit: UiMeasurementUnit) = unit.addRecordDialogTitleResId
            override fun getEditModeTitleTextResId(unit: UiMeasurementUnit) = unit.changeCountResId
            override val goalPickerEnabled = false
        },
        GoalPicker {
            override fun getTitleTextResId(unit: UiMeasurementUnit) = R.string.select_goal
            override val goalPickerEnabled = true
        };

        abstract val goalPickerEnabled: Boolean

        abstract fun getTitleTextResId(unit: UiMeasurementUnit): Int
        open fun getEditModeTitleTextResId(unit: UiMeasurementUnit) = getTitleTextResId(unit)
    }

    companion object {
        val goalTypeValues = arrayOf(null, *UiGoal.Type.values())
    }
}
