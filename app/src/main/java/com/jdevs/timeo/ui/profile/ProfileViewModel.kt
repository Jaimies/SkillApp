package com.jdevs.timeo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.SingleLiveEvent
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    val isSignedIn get() = _isSignedIn as LiveData<Boolean>
    private val _isSignedIn = MutableLiveData(false)

    val navigateToSignIn = SingleLiveEvent<Any>()
    val signOut = SingleLiveEvent<Any>()

    init {

        if (authRepository.isUserSignedIn) {

            _isSignedIn.value = true
        }
    }

    fun signOut() {

        authRepository.signOut()
        _isSignedIn.value = false
    }

    fun triggerSignOut() = signOut.call()
    fun navigateToSignIn() = navigateToSignIn.call()
}
