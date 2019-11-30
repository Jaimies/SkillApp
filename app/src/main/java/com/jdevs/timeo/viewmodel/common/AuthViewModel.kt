package com.jdevs.timeo.viewmodel.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.jdevs.timeo.repository.FirebaseAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("PropertyName")
abstract class AuthViewModel : ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val isLoading get() = _isLoading as LiveData<Boolean>
    val emailError get() = _emailError as LiveData<String>
    val passwordError get() = _passwordError as LiveData<String>

    private val _isLoading = MutableLiveData(false)
    private val _emailError = MutableLiveData("")
    private val _passwordError = MutableLiveData("")
    protected val authRepository by lazy { FirebaseAuthRepository() }

    fun setEmailError(error: String) {
        _passwordError.value = ""
        _emailError.value = error
    }

    fun setPasswordError(error: String) {
        _emailError.value = ""
        _passwordError.value = error
    }

    fun showLoader() {
        _isLoading.value = true
    }

    fun hideLoader() {
        _isLoading.value = false
    }

    protected fun launchSuspendingProcess(
        failure: (FirebaseException) -> Unit,
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
            } catch (exception: FirebaseException) {

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
