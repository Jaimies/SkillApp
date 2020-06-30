package com.jdevs.timeo.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {

    override var isSignedIn = false
        private set

    override val uid = ""

    fun signIn() {
        isSignedIn = true
    }

    override suspend fun createUser(email: String, password: String): SignUpResult {
        return signIn().let { SignUpResult.Success }
    }

    override suspend fun signIn(email: String, password: String): SignInResult {
        return signIn().let { SignInResult.Success }
    }

    override suspend fun signInWithGoogle(accountTask: Task<GoogleSignInAccount>): GoogleSignInResult {
        return signIn().let { GoogleSignInResult.Success }
    }

    override fun signOut() {
        isSignedIn = false
    }
}
