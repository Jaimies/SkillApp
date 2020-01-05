package com.jdevs.timeo.data.source

abstract class BaseRepository(
    private val authRepository: AuthRepository
) {

    protected val isUserSignedIn
        get() = authRepository.isUserSignedIn
}
