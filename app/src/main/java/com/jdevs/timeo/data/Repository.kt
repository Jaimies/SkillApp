package com.jdevs.timeo.data

import com.jdevs.timeo.domain.repository.AuthRepository

@Suppress("UnnecessaryAbstractClass")
abstract class Repository<T, Remote : T>(
    private val remoteDataSource: Remote,
    private val localDataSource: T,
    private val authRepository: AuthRepository
) {

    protected val isUserSignedIn
        get() = authRepository.isUserSignedIn

    protected val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource

    protected suspend fun performOnRemote(action: suspend (Remote) -> Unit) {

        if (isUserSignedIn) {

            action(remoteDataSource)
        }
    }
}
