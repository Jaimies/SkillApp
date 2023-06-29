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
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        bundle.putInt(FIRST_PICKER_VALUE, firstPicker.value)
        bundle.putLong(COUNT, count)
    }

    override fun getPickerValues(bundle: Bundle): Triple<Int, Int, Int> {
        val count = bundle.getLong(COUNT, 0)
        val pickerValues = getPickerValuesForValue(unit.toType(count))

        return Triple(
            first = bundle.getInt(FIRST_PICKER_VALUE, 0),
            second = pickerValues.first,
            third = pickerValues.second,
        )
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

    data class Configuration(
        val value: Value,
        val isInEditMode: Boolean = false,
        val overrideThemeResId: Int = 0,
    ) {
        fun getArguments() = Bundle().apply {
            putLong(COUNT, value.count)
            putInt(FIRST_PICKER_VALUE, goalTypeValues.indexOf((value.goalType ?: Goal.Type.Daily).mapToUI()))
            putBoolean(ENABLE_FIRST_PICKER, value.mode.goalPickerEnabled)
            putInt(TITLE_RES_EXTRA, value.mode.getTitleTextResId(value.unit.mapToUI(), isInEditMode))
        }
    }

    sealed class Value {
        abstract val count: Long
        abstract val goalType: Goal.Type?
        abstract val unit: MeasurementUnit<*>
        abstract val mode: Mode

        data class RegularValue(
            override val count: Long,
            override val unit: MeasurementUnit<*>,
        ) : Value() {
            override val goalType get() = null
            override val mode get() = Mode.ValuePicker
        }

        data class GoalValue(
            val goal: Goal?,
            override val unit: MeasurementUnit<*>,
        ): Value() {
            override val count get() = goal?.count ?: 0
            override val goalType get() = goal?.type
            override val mode get() = Mode.GoalPicker
        }
    }

    sealed class Mode {
        object ValuePicker: Mode() {
            override fun getViewModeTitleTextResId(unit: UiMeasurementUnit) = unit.addRecordDialogTitleResId
            override fun getEditModeTitleTextResId(unit: UiMeasurementUnit) = unit.changeCountResId
            override val goalPickerEnabled = false
        }

        object GoalPicker: Mode() {
            override fun getViewModeTitleTextResId(unit: UiMeasurementUnit) = R.string.select_goal
            override val goalPickerEnabled = true
        }

        abstract val goalPickerEnabled: Boolean

        abstract fun getViewModeTitleTextResId(unit: UiMeasurementUnit): Int
        open fun getEditModeTitleTextResId(unit: UiMeasurementUnit) = getViewModeTitleTextResId(unit)

        fun getTitleTextResId(unit: UiMeasurementUnit, isInEditMode: Boolean): Int {
            if (isInEditMode) return getViewModeTitleTextResId(unit)
            else return getEditModeTitleTextResId(unit)
        }
    }

    companion object {
        const val COUNT = "COUNT"

        val goalTypeValues = arrayOf(null, *UiGoal.Type.values())
    }
}
