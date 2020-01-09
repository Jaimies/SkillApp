package com.jdevs.timeo.util

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.MainActivity
import com.jdevs.timeo.TimeoApplication
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

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)

@Suppress("TooGenericExceptionCaught")
fun LoaderViewModel.launchSuspendingProcess(
    onFailure: (Exception) -> Unit = {},
    onSuccess: () -> Unit = {},
    block: suspend () -> Unit
) {

    showLoader()

    viewModelScope.launch {

        try {

            block()
            onSuccess()
        } catch (exception: Exception) {

            onFailure(exception)
        } finally {

            hideLoader()
        }
    }
}

fun Fragment.showSnackbar(@StringRes resId: Int) =
    Snackbar.make(requireView(), resId, Snackbar.LENGTH_LONG).show()

fun Fragment.navigateToGraph(@IdRes graphId: Int) {

    requireMainActivity()?.navigateToGraph(graphId)

    findNavController().apply {

        popBackStack(graph.id, true)
    }
}

fun <T> Fragment.observeEvent(event: SingleLiveEvent<T>, onEvent: (T?) -> Unit) {

    event.observeEvent(viewLifecycleOwner) { onEvent(it) }
}

fun Fragment.requireMainActivity() = requireActivity() as? MainActivity
fun Fragment.getCoroutineIoScope() = (requireActivity().application as TimeoApplication).ioScope
