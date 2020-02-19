package com.jdevs.timeo.ui.tasks

import android.graphics.Paint
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.util.Consumer
import androidx.databinding.BindingAdapter

@BindingAdapter("isCrossed")
fun setTextCrossed(textView: TextView, isCrossed: Boolean?) {

    textView.paintFlags = if (isCrossed == true) Paint.STRIKE_THRU_TEXT_FLAG else 0
}

@BindingAdapter("onChecked")
fun setOnCheckedListener(checkBox: CheckBox, onChecked: Consumer<Boolean>) {

    checkBox.setOnCheckedChangeListener { _, isChecked -> onChecked.accept(isChecked) }
}
