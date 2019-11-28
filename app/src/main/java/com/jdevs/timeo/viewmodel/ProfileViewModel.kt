package com.jdevs.timeo.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.repository.FirebaseAuthRepository

class ProfileViewModel : ViewModel() {
    var isUserLoggedIn = false
    var userEmail = ""

    var navigator: Navigator? = null

    private val firebaseAuthRepository by lazy { FirebaseAuthRepository() }

    fun logout() {
        firebaseAuthRepository.logout()
    }

    interface Navigator {
        fun login()
        fun logout()
    }
}
