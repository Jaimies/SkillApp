package com.jdevs.timeo.util

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.MainActivity
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.util.livedata.SingleLiveEvent

fun Fragment.showSnackbar(@StringRes resId: Int) =
    Snackbar.make(requireView(), resId, Snackbar.LENGTH_LONG).show()

fun Fragment.navigateToGraph(@IdRes graphId: Int) {

    mainActivity.navigateToGraph(graphId)

    findNavController().run {

        popBackStack(graph.id, true)
    }
}

inline fun <T> Fragment.observeEvent(event: SingleLiveEvent<T>, crossinline onEvent: (T?) -> Unit) {

    event.observeEvent(viewLifecycleOwner) { onEvent(it) }
}

@Suppress("UNCHECKED_CAST")
fun <T : Fragment> Fragment.findFragmentById(@IdRes id: Int) =
    requireActivity().supportFragmentManager.findFragmentById(id) as T

inline val Fragment.mainActivity get() = requireActivity() as MainActivity
inline val Fragment.application get() = requireActivity().application as TimeoApplication
inline val Fragment.appComponent get() = application.appComponent
