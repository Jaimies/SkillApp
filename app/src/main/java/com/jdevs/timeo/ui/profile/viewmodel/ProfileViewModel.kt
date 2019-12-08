package com.jdevs.timeo.ui.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.api.repository.auth.AuthRepository

class ProfileViewModel : ViewModel() {

    var navigator: Navigator? = null
    val email get() = _email as LiveData<String>
    val isLoggedIn get() = _isLoggedIn as LiveData<Boolean>

    private val _isLoggedIn = MutableLiveData(false)
    private val _email = MutableLiveData("")

    fun signOut() {

        AuthRepository.signOut()
        _isLoggedIn.value = false
    }

    fun signIn(email: String) {

        _isLoggedIn.value = true
        _email.value = email
    }

    interface Navigator {

        fun signIn()
        fun signOut()
    }
}
