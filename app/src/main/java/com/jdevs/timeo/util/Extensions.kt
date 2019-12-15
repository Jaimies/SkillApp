package com.jdevs.timeo.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.jdevs.timeo.MainActivity
import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.util.ViewConstants.HAS_TEXT_WATCHER
import kotlinx.coroutines.launch

fun <T> lazyUnsynchronized(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

@Suppress("EmptyFunctionBlock")
fun EditText.doOnceAfterTextChanged(block: () -> Unit) {

    if (tag == HAS_TEXT_WATCHER) {

        return
    }

    addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {

            block()
            tag = ""

            this@doOnceAfterTextChanged.removeTextChangedListener(this)
        }
    })

    tag = HAS_TEXT_WATCHER
}

fun Task<*>.logOnFailure(message: String = "Failed to perform an operation") {

    addOnFailureListener { Log.w(TAG, message, it) }
}

fun ViewGroup.inflate(layoutId: Int): View {

    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun ViewModel.launchSuspendingProcess(
    onFailure: (FirebaseException) -> Unit = {},
    onSuccess: () -> Unit = {},
    navigator: LoaderViewModel.Navigator? = null,
    logOnFailure: String = "",
    block: suspend () -> Unit
) {

    if (this is LoaderViewModel) {

        showLoader()
    }

    navigator?.hideKeyboard()

    viewModelScope.launch {

        try {

            block()
            onSuccess()
        } catch (exception: FirebaseException) {

            if (logOnFailure != "") {

                Log.w(TAG, logOnFailure, exception)
            }

            onFailure(exception)
        } finally {

            if (this@launchSuspendingProcess is LoaderViewModel) {

                hideLoader()
            }
        }
    }
}

fun Fragment.navigateToGraph(graphId: Int) {

    requireMainActivity().navigateToGraph(graphId)

    findNavController().apply {

        popBackStack(graph.id, true)
    }
}

fun Fragment.requireMainActivity() = requireActivity() as MainActivity
