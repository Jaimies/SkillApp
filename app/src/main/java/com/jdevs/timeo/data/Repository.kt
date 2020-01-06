package com.jdevs.timeo.data

import com.jdevs.timeo.data.auth.AuthRepository

@Suppress("UnnecessaryAbstractClass")
abstract class Repository<T, Remote : T>(
    private val remoteDataSource: Remote,
    private val localDataSource: T,
    protected val authRepository: AuthRepository
) {

    protected val isUserSignedIn
        get() = authRepository.isUserSignedIn

    protected val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource

    protected fun performOnRemote(action: (Remote) -> Unit) {

        if (isUserSignedIn) {

            action(remoteDataSource)
        }
    }

    protected suspend fun performOnRemoteSuspend(action: suspend (Remote) -> Unit) {

        if (isUserSignedIn) {

            action(remoteDataSource)
        }
    }
}
