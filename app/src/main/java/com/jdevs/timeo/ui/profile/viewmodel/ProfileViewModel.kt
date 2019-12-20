package com.jdevs.timeo.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.SingleLiveEvent

class ProfileViewModel : ViewModel() {

    val isSignedIn get() = _isSignedIn as LiveData<Boolean>
    val navigateToSignIn = SingleLiveEvent<Any>()
    val signOut = SingleLiveEvent<Any>()

    private val _isSignedIn = MutableLiveData(false)

    fun signOut() {

        AuthRepository.signOut()
        _isSignedIn.value = false
    }

    fun triggerSignOut() {

        signOut.call()
    }

    fun navigateToSignIn() {

        navigateToSignIn.call()
    }

    fun notifyUserIsSignedIn() {

        _isSignedIn.value = true
    }

    interface Navigator {

        fun signIn()
        fun signOut()
    }
}
