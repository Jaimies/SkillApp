package com.maxpoliakov.skillapp.shared.extensions

import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.text.InputType
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.core.view.children
import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.Field

private typealias Formatter = (value: Int) -> String

fun NumberPicker.setup() {
    setupScrollSpeed()
    findEditText()?.setRawInputType(InputType.TYPE_CLASS_NUMBER)
}

fun NumberPicker.setValues(numberOfValues: Int, formatter: Formatter) {
    setFormatter(formatter)
    minValue = 0
    maxValue = numberOfValues - 1

    adjustWidthToPreventValuesFromBeingClipped(numberOfValues, formatter)
    ensureInitialValuesAreFormatted()
}

private fun NumberPicker.adjustWidthToPreventValuesFromBeingClipped(numberOfValues: Int, formatter: Formatter) {
    layoutParams.width = getWidthOfLongestValue(numberOfValues, formatter) + 10.dp.toPx(context)
    requestLayout()
}

private fun NumberPicker.ensureInitialValuesAreFormatted() {
    findEditText()?.filters = arrayOf()
}

private fun NumberPicker.getWidthOfLongestValue(numberOfValues: Int, formatter: Formatter): Int {
    return getWidthOfString(getLongestValue(numberOfValues, formatter))
}

private fun getLongestValue(numberOfValues: Int, formatter: Formatter): String{
    // we can afford to search through 10 values,
    if (numberOfValues <= 10) {
        return List(numberOfValues, formatter).maxBy(String::length)
    }

    // but if there's more, we have to assume the last value is the longest
    return formatter(numberOfValues - 1)
}

private fun NumberPicker.getWidthOfString(string: String): Int {
    val paint = Paint().also {
        // todo polyfill
        it.textSize = textSize
    }

    Rect().run {
        paint.getTextBounds(string, 0, string.length, this)
        return width()
    }
}

fun NumberPicker.setKeyboardInputEnabled(enabled: Boolean) {
    if (enabled) enableKeyboardInput()
    else disableKeyboardInput()
}

fun NumberPicker.enableKeyboardInput() {
    descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS
}

fun NumberPicker.disableKeyboardInput() {
    descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
}

private fun ViewGroup.findEditText(): EditText? {
    for (child in children) {
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