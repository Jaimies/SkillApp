package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    @JvmField
    var isUserLoggedIn = false
    @JvmField
    var userEmail = ""

    var navigator: Navigator? = null

    interface Navigator {
        fun login()
        fun logout()
    }
}
