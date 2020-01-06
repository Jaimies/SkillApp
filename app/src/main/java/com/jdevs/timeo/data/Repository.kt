package com.jdevs.timeo.data

import com.jdevs.timeo.data.auth.AuthRepository

@Suppress("UnnecessaryAbstractClass")
abstract class Repository<Remote : DataSource, DataSource>(
    private val remoteDataSource: Remote,
    private val localDataSource: DataSource,
    private val authRepository: AuthRepository
) {

    private val isUserSignedIn
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
