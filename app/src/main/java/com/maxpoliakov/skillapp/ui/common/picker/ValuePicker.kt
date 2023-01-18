package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

abstract class ValuePicker<T>(private val unit: MeasurementUnit<T>) : PickerDialog() {
    abstract val value: T

    fun addOnConfirmedListener(listener: (count: Long) -> Unit) {
        addOnPositiveButtonClickListener {
            listener(unit.toLong(value))
        }
    }

    abstract class Builder : PickerDialog.Builder<Builder, ValuePicker<*>>() {
        abstract val titleTextInEditModeResId: Int

        fun setEditModeEnabled(isInEditMode: Boolean): Builder {
            setTitleText(if (isInEditMode) titleTextInEditModeResId else titleTextResId)
            return this
        }

        abstract fun setCount(count: Long): Builder
    }
}
