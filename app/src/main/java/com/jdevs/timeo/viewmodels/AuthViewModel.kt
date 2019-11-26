package com.jdevs.timeo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.jdevs.timeo.repositories.FirebaseAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class AuthViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading get() = _isLoading as LiveData<Boolean>

    protected val authRepository by lazy { FirebaseAuthRepository() }

    fun showLoader() {
        _isLoading.value = true
    }

    fun hideLoader() {
        _isLoading.value = false
    }

    protected fun launchSuspendingProcess(
        failure: (Exception) -> Unit,
        success: () -> Unit,
        navigator: Navigator?,
        block: suspend () -> Unit
    ) {
        showLoader()
        navigator?.hideKeyboard()

        viewModelScope.launch(Dispatchers.Main) {
            try {

                block()
                success()
            } catch (exception: FirebaseAuthException) {

                failure(exception)
            } finally {

                hideLoader()
            }
        }
    }

    interface Navigator {
        fun hideKeyboard()
    }
}
