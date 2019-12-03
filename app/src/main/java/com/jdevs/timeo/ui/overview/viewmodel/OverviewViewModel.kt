package com.jdevs.timeo.ui.overview.viewmodel

import com.jdevs.timeo.api.repository.auth.AuthRepository
import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.util.launchSuspendingProcess

class OverviewViewModel : LoaderViewModel() {
    var navigator: Navigator? = null

    private val authRepository by lazy { AuthRepository() }

    fun signInAnonymously(logOnFailure: String = "") {

        launchSuspendingProcess(
            navigator = navigator,
            logOnFailure = logOnFailure,
            showLoader = false
        ) {

            authRepository.signInAnonymously()
        }
    }
}
