package com.jdevs.timeo.ui.tasks

import android.graphics.Paint
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("isCrossed")
fun TextView.setTextCrossed(isCrossed: Boolean?) {

    paintFlags = if (isCrossed == true) Paint.STRIKE_THRU_TEXT_FLAG else 0
}
