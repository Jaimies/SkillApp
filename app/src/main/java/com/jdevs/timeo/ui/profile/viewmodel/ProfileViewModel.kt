package com.jdevs.timeo.ui.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.api.repository.auth.AuthRepository

class ProfileViewModel : ViewModel() {
    var navigator: Navigator? = null
    var isUserLoggedIn = false
    var userEmail = ""

    private val authRepository by lazy { AuthRepository() }

    fun logout() {
        authRepository.logout()
    }

    interface Navigator {
        fun login()
        fun logout()
    }
}
