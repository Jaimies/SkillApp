package com.jdevs.timeo.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fakes the Firebase Authentication repository
 */
@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {

    /**
     * Indicates whether the user is signed in
     */
    override var isUserSignedIn = false
        private set

    override val uid = ""

    /**
     * Fakes signed in state
     */
    fun signIn() {

        isUserSignedIn = true
    }

    /**
     * The following three methods fake the signed in state
     */
    override suspend fun createUser(email: String, password: String): SignUpResult {
        signIn()
        return SignUpResult.Success
    }

    override suspend fun signIn(email: String, password: String): SignInResult {
        signIn()
        return SignInResult.Success
    }

    override suspend fun signInWithGoogle(accountTask: Task<GoogleSignInAccount>): GoogleSignInResult {
        signIn()
        return GoogleSignInResult.Success
    }

    /**
     * Fakes not signed in state
     */
    override fun signOut() {

        isUserSignedIn = false
    }
}
