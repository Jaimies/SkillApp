package com.maxpoliakov.skillapp.ui.common.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

abstract class ValuePicker<T>(private val unit: MeasurementUnit<T>) : PickerDialog() {
    abstract val value: T

    fun addOnConfirmedListener(listener: (count: Long) -> Unit) {
        addOnPositiveButtonClickListener {
            listener(unit.toLong(value))
        }
    }

    abstract class Builder<T>(private val unit: MeasurementUnit<T>) : PickerDialog.Builder<Builder<T>, ValuePicker<*>>() {
        abstract val titleTextInEditModeResId: Int

        protected abstract fun setValue(value: T)

        fun setEditModeEnabled(isInEditMode: Boolean): Builder<T> {
            setTitleText(if (isInEditMode) titleTextInEditModeResId else titleTextResId)
            return this
        }

        fun setCount(count: Long): Builder<T> {
            setValue(unit.toType(count))
            return this
        }
    }
}
