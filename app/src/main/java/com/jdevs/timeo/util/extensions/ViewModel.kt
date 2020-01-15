package com.jdevs.timeo.util.extensions

import androidx.lifecycle.viewModelScope
import com.jdevs.timeo.ui.common.viewmodel.LoaderViewModel
import kotlinx.coroutines.launch

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
