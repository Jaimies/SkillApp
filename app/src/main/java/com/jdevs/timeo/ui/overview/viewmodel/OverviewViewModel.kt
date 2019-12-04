package com.jdevs.timeo.ui.overview.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.api.repository.auth.AuthRepository
import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.util.launchSuspendingProcess

class OverviewViewModel : ViewModel() {
    var navigator: LoaderViewModel.Navigator? = null

    private val authRepository by lazy { AuthRepository() }

    fun signInAnonymously(logOnFailure: String = "") {

        launchSuspendingProcess(
            navigator = navigator,
            logOnFailure = logOnFailure
        ) {

            authRepository.signInAnonymously()
        }
    }
}
