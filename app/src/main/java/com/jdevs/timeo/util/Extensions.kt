package com.jdevs.timeo.util

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun <T> lazyUnsynchronized(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun TextWatcher.removeSelfFrom(editText: EditText) {
    editText.removeTextChangedListener(this)
}

fun EditText.doOnceAfterTextChanged(block: () -> Unit) {

    addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            block()
            removeSelfFrom(this@doOnceAfterTextChanged)
        }
    })
}

fun Task<*>.logOnFailure(message: String = "Failed to perform an operation") {

    addOnFailureListener { Log.w(TAG, message, it) }
}

fun ViewGroup.inflate(layoutId: Int): View {

    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun LoaderViewModel.launchSuspendingProcess(
    onFailure: (FirebaseException) -> Unit = {},
    onSuccess: () -> Unit = {},
    navigator: LoaderViewModel.Navigator?,
    logOnFailure: String = "",
    block: suspend () -> Unit
) {
    showLoader()
    navigator?.hideKeyboard()

    viewModelScope.launch(Dispatchers.Main) {
        try {

            block()
            onSuccess()
        } catch (exception: FirebaseException) {

            if (logOnFailure != "") {
                Log.w(TAG, logOnFailure, exception)
            }

            onFailure(exception)
        } finally {

            hideLoader()
        }
    }
}
