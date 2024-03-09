package com.maxpoliakov.skillapp.domain.repository

import com.maxpoliakov.skillapp.domain.model.User

interface AuthRepository {
    val currentUser: User?
    val hasAppDataPermission: Boolean

    fun addSignInListener(listener: SignInListener)
    fun addSignOutListener(listener: SignOutListener)

    fun removeSignInListener(listener: SignInListener)
    fun removeSignOutListener(listener: SignOutListener)

    fun signOut()

    fun interface SignInListener {
        fun onSignedIn()
    }

    fun interface SignOutListener {
        fun onSignedOut()
    }
}
