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

abstract class ValuePicker<T>(protected val unit: MeasurementUnit<T>) : PickerDialog(), NumberPicker.OnValueChangeListener {
    abstract val value: T
    val maxValue get() = unit.toType((goalType ?: UiGoal.Type.Daily).getMaximumCount(unit))

    abstract fun getPickerValuesForValue(value: T): Pair<Int, Int>

    val count get() = unit.toLong(value)

    val goalType get() = goalTypeValues[firstPicker.value]
    val goal get() = goalType?.let { goalType -> Goal(unit.toLong(value), goalType.toDomain()) }

    override val numberOfFirstPickerValues get() = goalTypeValues.size
    final override val numberOfSecondPickerValues get() = getPickerValuesForValue(maxValue).first + 1
    final override val numberOfThirdPickerValues get() = getPickerValuesForValue(maxValue).second + 1

    override fun formatFirstPickerValue(value: Int): String {
        return requireContext().getString(goalTypeValues[value]?.goalResId ?: R.string.no_plan)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (firstPickerEnabled) setupFirstPicker()
    }

    private fun setupFirstPicker() {
        firstPicker.disableKeyboardInput()
        firstPicker.wrapSelectorWheel = false
        firstPicker.setOnValueChangedListener(this)
        this.onValueChange(firstPicker, 0, firstPicker.value)
    }

    override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
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
