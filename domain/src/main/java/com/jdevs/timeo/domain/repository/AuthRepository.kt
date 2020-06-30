package com.jdevs.timeo.domain.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.model.result.SignUpResult

interface AuthRepository {

    val isSignedIn: Boolean
    val uid: String?

    suspend fun createUser(email: String, password: String): SignUpResult

    suspend fun signIn(email: String, password: String): SignInResult

    suspend fun signInWithGoogle(accountTask: Task<GoogleSignInAccount>): GoogleSignInResult

    fun signOut()
}
