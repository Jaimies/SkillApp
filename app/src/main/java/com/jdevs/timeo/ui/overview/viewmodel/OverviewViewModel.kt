package com.jdevs.timeo.ui.overview.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.launchSuspendingProcess

class OverviewViewModel : ViewModel() {

    fun signInIfNeeded(logOnFailure: String = "") {

        if (!AuthRepository.isUserSignedIn) {

            launchSuspendingProcess(logOnFailure = logOnFailure) {

                AuthRepository.signInAnonymously()
            }
        }
    }
}
