package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import javax.inject.Inject

class StubAuthRepository @Inject constructor(): AuthRepository {
    override val currentUser get() = User("someuser@gmail.com")
    override val hasAppDataPermission get() = true

    override fun signOut() {}

    override fun addSignInListener(listener: AuthRepository.SignInListener) {}
    override fun addSignOutListener(listener: AuthRepository.SignOutListener) {}
    override fun removeSignInListener(listener: AuthRepository.SignInListener) {}
    override fun removeSignOutListener(listener: AuthRepository.SignOutListener) {}
}
