package com.maxpoliakov.skillapp.util.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.inflateDataBinding(@LayoutRes resId: Int): T {
    return DataBindingUtil.inflate<T>(LayoutInflater.from(context), resId, this, false).apply {
        lifecycleOwner = fragmentActivity
    }
}