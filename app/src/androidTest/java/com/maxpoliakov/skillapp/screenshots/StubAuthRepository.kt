package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import javax.inject.Inject

class StubAuthRepository @Inject constructor(): AuthRepository {
    override val currentUser get() = User("someuser@gmail.com")
    override val hasAppDataPermission get() = true

    override fun signOut() {}
}
