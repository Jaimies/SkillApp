package com.jdevs.timeo.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface AuthRepository {

    val isUserSignedIn: Boolean
    val uid: String?

    suspend fun createAccount(email: String, password: String)

    suspend fun signIn(email: String, password: String)

    suspend fun linkGoogleAccount(account: GoogleSignInAccount)

    fun signOut()
}
