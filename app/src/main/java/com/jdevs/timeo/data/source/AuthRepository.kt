package com.jdevs.timeo.data.source

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult

interface AuthRepository {

    val isUserSignedIn: Boolean
    val uid: String?

    suspend fun createAccount(email: String, password: String): AuthResult

    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun linkGoogleAccount(account: GoogleSignInAccount): AuthResult

    fun signOut()
}
