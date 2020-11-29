package com.maxpoliakov.skillapp.util.fragment

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG

fun Fragment.snackbar(@StringRes resId: Int) =
    Snackbar.make(requireView(), resId, LENGTH_LONG).show()

fun Fragment.navigate(@IdRes resId: Int) {
    findNavController().navigate(resId)
}

fun Fragment.setTitle(title: CharSequence) {
    val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
    actionBar?.title = title
}

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline onChange: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { onChange(it) }
}
