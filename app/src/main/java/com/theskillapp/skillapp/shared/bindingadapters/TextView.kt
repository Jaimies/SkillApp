package com.theskillapp.skillapp.shared.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.theskillapp.skillapp.shared.extensions.getColorAttributeValue

@BindingAdapter("textColorAttr")
fun TextView.setTextColorAttr(attrValue: Int) {
    setTextColor(context.getColorAttributeValue(attrValue))
}
