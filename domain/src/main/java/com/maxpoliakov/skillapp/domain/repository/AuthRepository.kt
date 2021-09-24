package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.User

interface AuthRepository {
    val currentUser: User?
    val hasAppDataPermission: Boolean

    fun signOut()
}
