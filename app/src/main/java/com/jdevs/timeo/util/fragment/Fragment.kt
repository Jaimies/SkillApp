package com.jdevs.timeo.util.fragment

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG
import com.jdevs.timeo.ui.MainActivity

fun Fragment.snackbar(@StringRes resId: Int) =
    Snackbar.make(requireView(), resId, LENGTH_LONG).show()

fun Fragment.navigateToGraph(@IdRes graphId: Int) {

    mainActivity.navigateToGraph(graphId)

    findNavController().run {
        popBackStack(graph.id, true)
    }
}

fun Fragment.navigate(@IdRes resId: Int) {
    findNavController().navigate(resId)
}

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline onChange: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner) { onChange(it) }
}

val Fragment.mainActivity get() = requireActivity() as MainActivity
