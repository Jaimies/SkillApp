package com.jdevs.timeo.ui.profile.viewmodel

import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.util.launchSuspendingProcess

class SignUpViewModel : AuthViewModel() {

    var navigator: Navigator? = null

    fun createAccount(
        email: String,
        password: String,
        onFailure: (Exception) -> Unit = {},
        onSuccess: () -> Unit = {}
    ) {

        launchSuspendingProcess(onFailure, onSuccess, navigator) {

            AuthRepository.createAccount(email, password)
        }
    }

    fun triggerSignUp() {

        navigator?.signUp(email.value.orEmpty(), password.value.orEmpty())
    }

    override fun onFragmentDestroyed() {

        navigator = null
    }

    interface Navigator : LoaderViewModel.Navigator {

        fun navigateToSignIn()
        fun signUp(email: String, password: String)
    }
}
