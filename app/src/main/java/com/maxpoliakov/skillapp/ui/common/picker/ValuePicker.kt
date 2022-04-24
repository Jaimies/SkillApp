package com.maxpoliakov.skillapp.ui.common.picker

abstract class ValuePicker: PickerDialog() {
    abstract val count: Long

    abstract class Builder: PickerDialog.Builder() {
        abstract fun setCount(count: Long): Builder
        override fun build() = super.build() as ValuePicker
    }
}
