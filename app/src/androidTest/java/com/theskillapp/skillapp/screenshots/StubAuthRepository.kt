package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.domain.model.User
import com.theskillapp.skillapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class StubAuthRepository @Inject constructor(): AuthRepository {
    override val currentUser get() = flowOf(User("someuser@gmail.com", true))

    override fun reportSignIn() {}
    override fun signOut() {}

    override fun addSignInListener(listener: AuthRepository.SignInListener) {}
    override fun addSignOutListener(listener: AuthRepository.SignOutListener) {}
    override fun removeSignInListener(listener: AuthRepository.SignInListener) {}
    override fun removeSignOutListener(listener: AuthRepository.SignOutListener) {}
}
