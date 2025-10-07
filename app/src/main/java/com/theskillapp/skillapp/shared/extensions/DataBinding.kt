package com.theskillapp.skillapp.shared.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.inflateDataBinding(@LayoutRes resId: Int): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), resId, this, false)
}
