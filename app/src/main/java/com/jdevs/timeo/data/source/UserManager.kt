package com.jdevs.timeo.data.source

interface UserManager {

    val isUserSignedIn: Boolean

    fun signOut()
}
