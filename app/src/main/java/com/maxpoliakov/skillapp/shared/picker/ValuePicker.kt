package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.domain.model.MeasurementUnit

abstract class ValuePicker<T>(private val unit: MeasurementUnit<T>) : PickerDialog() {
    abstract val value: T

    fun addOnConfirmedListener(listener: (count: Long) -> Unit) {
        addOnPositiveButtonClickListener {
            listener(unit.toLong(value))
        }
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
            val value = unit.toType(count)
            if (value > maxValue) setValue(maxValue)
            else setValue(value)
            return this
        }
    }
}
