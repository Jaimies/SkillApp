package com.jdevs.timeo.ui.overview.viewmodel

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.common.viewmodel.LoaderViewModel
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.launchSuspendingProcess

class OverviewViewModel : ViewModel() {

    var navigator: LoaderViewModel.Navigator? = null

    fun signInIfNeeded(logOnFailure: String = "") {

        if (!AuthRepository.isUserSignedIn) {

            launchSuspendingProcess(navigator = navigator, logOnFailure = logOnFailure) {

                AuthRepository.signInAnonymously()
            }
        }
    }
}
