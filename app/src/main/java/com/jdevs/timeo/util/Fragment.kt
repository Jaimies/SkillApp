package com.jdevs.timeo.util

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.MainActivity
import com.jdevs.timeo.TimeoApplication

fun Fragment.showSnackbar(@StringRes resId: Int) =
    Snackbar.make(requireView(), resId, Snackbar.LENGTH_LONG).show()

fun Fragment.navigateToGraph(@IdRes graphId: Int) {

    mainActivity.navigateToGraph(graphId)

    findNavController().run {

        popBackStack(graph.id, true)
    }
}

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline onEvent: (T) -> Unit) {

    liveData.observe(viewLifecycleOwner) { onEvent(it) }
}

inline val Fragment.mainActivity get() = requireActivity() as MainActivity
inline val Fragment.application get() = requireActivity().application as TimeoApplication
inline val Fragment.appComponent get() = application.appComponent
