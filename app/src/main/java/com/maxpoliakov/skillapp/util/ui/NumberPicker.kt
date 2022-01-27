package com.maxpoliakov.skillapp.util.ui

import android.graphics.Typeface
import android.text.TextPaint
import cn.carbswang.android.numberpickerview.library.NumberPickerView

fun NumberPickerView.setFontFamily(fontFamily: String) {
    val field = this::class.java.getDeclaredField("mPaintText")
    field.isAccessible = true
    val paint = field.get(this) as TextPaint
    paint.typeface = Typeface.create(fontFamily, Typeface.NORMAL)
}

fun NumberPickerView.setup(displayedValues: Array<String>) {
    this.displayedValues = displayedValues
    minValue = 0
    maxValue = displayedValues.lastIndex
    setFriction(0.03f)
    setFontFamily("sans-serif-medium")
}
