package com.jdevs.timeo.ui.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.util.lifecycle.launchCoroutine

open class LoadingViewModel(isLoadingByDefault: Boolean = false) : KeyboardHidingViewModel() {

    val isLoading get() = _isLoading as LiveData<Boolean>
    private val _isLoading = MutableLiveData(isLoadingByDefault)

    fun showLoader() {

        _isLoading.value = true
    }

    fun hideLoader() {

        _isLoading.value = false
    }

    inline fun <TResult : Any> launchSuspendingProcess(
        crossinline onResult: (TResult) -> Unit,
        crossinline isSuccess: (TResult) -> Boolean,
        crossinline block: suspend () -> TResult
    ) = launchCoroutine {
        showLoader()
        val result = block().also { onResult(it) }
        if (!isSuccess(result)) hideLoader()
    }
}
