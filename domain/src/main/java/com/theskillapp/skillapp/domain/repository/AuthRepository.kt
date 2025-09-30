package com.theskillapp.skillapp.domain.repository

import com.theskillapp.skillapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>

    fun addSignInListener(listener: SignInListener)
    fun addSignOutListener(listener: SignOutListener)

    fun removeSignInListener(listener: SignInListener)
    fun removeSignOutListener(listener: SignOutListener)

    fun reportSignIn()
    fun signOut()

    fun interface SignInListener {
        fun onSignedIn()
    }

    fun interface SignOutListener {
        fun onSignedOut()
    }
}
