package com.jdevs.timeo.ui.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.usecase.auth.GetAuthStateUseCase
import com.jdevs.timeo.domain.usecase.auth.SignOutUseCase
import com.jdevs.timeo.domain.usecase.stats.GetStatsUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.ui.stats.StatsViewModel

class ProfileViewModel @ViewModelInject constructor(
    private val signOutUseCase: SignOutUseCase,
    getStatsUseCase: GetStatsUseCase,
    authState: GetAuthStateUseCase
) : StatsViewModel(getStatsUseCase) {

    val isSignedIn get() = _isSignedIn as LiveData<Boolean>
    private val _isSignedIn = MutableLiveData(authState.isSignedIn)

    val navigateToSignIn = SingleLiveEvent<Any>()
    val navigateToOverview: LiveData<Any> get() = _navigateToOverview
    private val _navigateToOverview = SingleLiveEvent<Any>()

    fun signOut() {
        signOutUseCase.invoke()
        _navigateToOverview.call()
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
