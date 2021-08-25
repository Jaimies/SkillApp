package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.User

interface AuthRepository {
    val currentUser: User?

    fun signOut()
}
