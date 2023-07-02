package com.maxpoliakov.skillapp.shared.extensions

import android.graphics.Paint
import android.graphics.Rect
import android.os.Build
import android.text.InputType
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.Formatter
import androidx.core.view.children
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.reflect.Field

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
    layoutParams.width = getWidthOfLongestValue(numberOfValues, formatter).toInt()
    requestLayout()
}

private fun NumberPicker.ensureInitialValuesAreFormatted() {
    findEditText()?.filters = arrayOf()
}

private fun NumberPicker.getWidthOfLongestValue(numberOfValues: Int, formatter: Formatter): Float {
    // we can afford to search through 10 values,
    if (numberOfValues <= 10) {
        return List(numberOfValues, formatter::format).maxOf(::getWidthOfString)
    }

    // but if there's more, we have to assume the last value is the longest
    return getWidthOfString(formatter.format(numberOfValues - 1))
}

private fun NumberPicker.getWidthOfString(string: String): Float {
    val paint = TextPaint().also {
        it.textSize = getTextSizeCompat().toFloat()
    }

    return paint.measureText(string)
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

// NumberPicker.getTextSize() only available in API 29+
private fun NumberPicker.getTextSizeCompat(): Int {
    return context.getDimensionAttribute(android.R.attr.textSize)
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