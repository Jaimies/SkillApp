package com.maxpoliakov.skillapp.util.ui

import android.widget.NumberPicker

fun NumberPicker.setup(displayedValues: Array<String>) {
    setValues(displayedValues)
    setFriction(0.03f)
}

fun NumberPicker.setValues(displayedValues: Array<String>) {
    minValue = 0
    maxValue = displayedValues.lastIndex
    setFormatter { value ->
        displayedValues.getOrNull(value) ?: ""
    }
    invalidate()
}

fun setFriction(friction: Float) {
    // todo
}
