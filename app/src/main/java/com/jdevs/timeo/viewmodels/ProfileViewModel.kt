package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.navigators.ProfileNavigator

class ProfileViewModel : ViewModel() {
    @JvmField
    var isUserLoggedIn = false
    @JvmField
    var userEmail = ""

    var navigator: ProfileNavigator? = null
}
