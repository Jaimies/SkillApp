package com.jdevs.timeo.util.extensions

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.MainActivity
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.util.SingleLiveEvent

fun Fragment.showSnackbar(@StringRes resId: Int) =
    Snackbar.make(requireView(), resId, Snackbar.LENGTH_LONG).show()

fun Fragment.navigateToGraph(@IdRes graphId: Int) {

    requireMainActivity().navigateToGraph(graphId)

    findNavController().run {

        popBackStack(graph.id, true)
    }
}

inline fun <T> Fragment.observeEvent(event: SingleLiveEvent<T>, crossinline onEvent: (T?) -> Unit) {

    event.observeEvent(viewLifecycleOwner) { onEvent(it) }
}

fun Fragment.requireMainActivity() = requireActivity() as MainActivity
fun Fragment.getApplication() = requireActivity().application as TimeoApplication
fun Fragment.getCoroutineIoScope() = getApplication().ioScope
fun Fragment.getAppComponent() = getApplication().appComponent
