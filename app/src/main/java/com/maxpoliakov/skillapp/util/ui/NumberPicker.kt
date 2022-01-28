package com.maxpoliakov.skillapp.util.ui

import android.graphics.Typeface
import cn.carbswang.android.numberpickerview.library.NumberPickerView

fun NumberPickerView.setFontFamily(fontFamily: String) {
    setContentTextTypeface(Typeface.create(fontFamily, Typeface.NORMAL))
}

fun NumberPickerView.setup(displayedValues: Array<String>) {
    refreshByNewDisplayedValues(displayedValues)
    setFriction(0.03f)
    setFontFamily("sans-serif-medium")
}
