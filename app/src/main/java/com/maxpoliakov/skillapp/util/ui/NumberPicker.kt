package com.maxpoliakov.skillapp.util.ui

import android.os.Build
import android.text.InputType
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.Field

fun NumberPicker.setup(displayedValues: Array<String>) {
    setupScrollSpeed()
    setValues(displayedValues)
}

fun NumberPicker.setValues(displayedValues: Array<String>) {
    minValue = 0
    maxValue = displayedValues.size - 1
    this.displayedValues = displayedValues
    setRawInputType(InputType.TYPE_CLASS_NUMBER)
}

fun NumberPicker.setRawInputType(type: Int) {
    findEditText()?.setRawInputType(type)
}

private fun ViewGroup.findEditText(): EditText? {
    val count = childCount
    for (i in 0 until count) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            return child.findEditText()
        } else if (child is EditText) {
            return child
        }
    }
    return null
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