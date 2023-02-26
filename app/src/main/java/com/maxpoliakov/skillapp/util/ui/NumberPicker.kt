package com.maxpoliakov.skillapp.util.ui

import android.os.Build
import android.view.ViewConfiguration
import android.widget.NumberPicker
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.Field

fun NumberPicker.setup(displayedValues: Array<String>) {
    setupScrollSpeed()
    setValues(displayedValues)
}

fun NumberPicker.setValues(displayedValues: Array<String>) {
    minValue = 0
    maxValue = displayedValues.lastIndex
    setFormatter { value ->
        displayedValues.getOrNull(value) ?: ""
    }
    invalidate()
}

private const val NUMBER_PICKER_MAX_FLING_VELOCITY_FIELD = "mMaximumFlingVelocity"

fun NumberPicker.setupScrollSpeed() {
    try {
        val field = getDeclaredNonSdkField(NUMBER_PICKER_MAX_FLING_VELOCITY_FIELD)
        field.isAccessible = true
        field.setInt(this, ViewConfiguration.get(context).scaledMaximumFlingVelocity)
    } catch (e: Exception) {
    }
}

private fun Any.getDeclaredNonSdkField(fieldName: String): Field {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
        return javaClass.getDeclaredField(fieldName)
    } else {
        return (HiddenApiBypass.getInstanceFields(javaClass) as List<Field>)
            .first { it.name == fieldName }
    }
}