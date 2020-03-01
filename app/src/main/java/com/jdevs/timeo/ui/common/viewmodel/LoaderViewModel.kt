package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.util.lifecycle.launchCoroutine

open class LoaderViewModel(isLoadingByDefault: Boolean = false) : KeyboardHidingViewModel() {

    val isLoading get() = _isLoading as LiveData<Boolean>
    private val _isLoading = MutableLiveData(isLoadingByDefault)

    fun showLoader() {

        _isLoading.value = true
    }

    fun hideLoader() {

        _isLoading.value = false
    }

    @Suppress("TooGenericExceptionCaught")
    inline fun launchSuspendingProcess(
        crossinline onFailure: (Exception) -> Unit = {},
        crossinline onSuccess: () -> Unit = {},
        crossinline block: suspend () -> Unit
    ) {

        showLoader()

        launchCoroutine {

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
}
