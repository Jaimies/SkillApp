package com.jdevs.timeo.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.auth.AuthRepository

class ProfileViewModel : ViewModel() {

    var navigator: Navigator? = null
    val isSignedIn get() = _isSignedIn as LiveData<Boolean>

    private val _isSignedIn = MutableLiveData(false)

    fun signOut() {

        AuthRepository.signOut()
        _isSignedIn.value = false
    }

    fun notifyUserIsSignedIn() {

        _isSignedIn.value = true
    }

    interface Navigator {

        fun signIn()
        fun signOut()
    }
}
