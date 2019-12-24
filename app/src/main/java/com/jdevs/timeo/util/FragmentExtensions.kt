package com.jdevs.timeo.util

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.MainActivity
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.ViewModelFactory

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as TimeoApplication).repository
    return ViewModelFactory(repository)
}

fun Fragment.navigateToGraph(graphId: Int) {

    requireMainActivity().navigateToGraph(graphId)

    findNavController().apply {

        popBackStack(graph.id, true)
    }
}

fun <T> Fragment.observeEvent(event: SingleLiveEvent<T>, onEvent: (T?) -> Unit) {

    event.observeEvent(viewLifecycleOwner) { onEvent(it) }
}

fun Fragment.requireMainActivity() = requireActivity() as MainActivity
fun Fragment.getCoroutineIoScope() = (requireActivity().application as TimeoApplication).ioScope
