package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.repositories.FirebaseAuthRepository

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
