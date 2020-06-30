package com.jdevs.timeo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.usecase.auth.GetAuthStateUseCase
import com.jdevs.timeo.domain.usecase.auth.SignOutUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    authState: GetAuthStateUseCase
) : ViewModel() {

    val isSignedIn get() = _isSignedIn as LiveData<Boolean>
    private val _isSignedIn = MutableLiveData(authState.isSignedIn)

    val navigateToSignIn = SingleLiveEvent<Any>()
    val navigateToOverview: LiveData<Any> get() = _navigateToOverview
    private val _navigateToOverview = SingleLiveEvent<Any>()

    fun triggerSignOut() {
        signOutUseCase.invoke()
        _navigateToOverview.call()
    }

    fun navigateToSignIn() = navigateToSignIn.call()
}
