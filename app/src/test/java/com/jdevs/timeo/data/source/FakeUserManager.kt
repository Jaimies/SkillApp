package com.jdevs.timeo.data.source

object FakeUserManager : UserManager {

    override var isUserSignedIn = true
        private set

    fun signIn() {

        isUserSignedIn = true
    }

    fun signOut() {

        isUserSignedIn = false
    }
}