package com.jdevs.timeo.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jdevs.timeo.util.view.fragmentActivity

fun <T : ViewDataBinding> ViewGroup.inflateDataBinding(@LayoutRes resId: Int): T {
    return DataBindingUtil.inflate<T>(LayoutInflater.from(context), resId, this, false).apply {
        lifecycleOwner = fragmentActivity
    }
}
