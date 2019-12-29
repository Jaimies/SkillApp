package com.jdevs.timeo.data.source

/**
 * Fakes the Firebase Authentication user manager
 */
object FakeAuthRepository : AuthRepository {

    /**
     * Indicates whether the user is signed in
     */
    override var isUserSignedIn = true
        private set

    /**
     * Fakes signed in state
     */
    fun signIn() {

        isUserSignedIn = true
    }

    /**
     * Fakes not signed in state
     */
    override fun signOut() {

        isUserSignedIn = false
    }
}
