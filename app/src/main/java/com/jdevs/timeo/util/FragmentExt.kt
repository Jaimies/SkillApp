package com.jdevs.timeo.util

import androidx.fragment.app.Fragment
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as TimeoApplication).repository
    return ViewModelFactory(repository)
}