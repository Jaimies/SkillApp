package com.jdevs.timeo.data

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jdevs.timeo.data.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fakes the Firebase Authentication repository
 */
@Singleton
class FakeAuthRepository @Inject constructor() :
    AuthRepository {

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
    override suspend fun createAccount(email: String, password: String) = signIn()

    override suspend fun signIn(email: String, password: String) = signIn()

    override suspend fun linkGoogleAccount(account: GoogleSignInAccount) = signIn()

    /**
     * Fakes not signed in state
     */
    override fun signOut() {

        isUserSignedIn = false
    }
}
