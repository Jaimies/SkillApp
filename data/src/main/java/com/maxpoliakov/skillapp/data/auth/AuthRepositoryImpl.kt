package com.maxpoliakov.skillapp.data.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient,
) : AuthRepository {
    override val currentUser: User?
        get() {
            val googleAccount = GoogleSignIn.getLastSignedInAccount(context) ?: return null
            return User(googleAccount.email.orEmpty())
        }

    override fun signOut() {
        googleSignInClient.signOut()
    }
}
