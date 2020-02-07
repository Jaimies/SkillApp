package com.jdevs.timeo.data

import com.jdevs.timeo.domain.repository.AuthRepository

@Suppress("UnnecessaryAbstractClass")
abstract class Repository<T, Remote : T>(
    private val remoteDataSource: Remote,
    private val localDataSource: T,
    private val authRepository: AuthRepository
) {

    private val isUserSignedIn
        get() = authRepository.isUserSignedIn

    protected val currentDataSource
        get() = if (isUserSignedIn) remoteDataSource else localDataSource
}
