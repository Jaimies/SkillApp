package com.jdevs.timeo.data

import com.jdevs.timeo.domain.repository.AuthRepository

@Suppress("UnnecessaryAbstractClass")
abstract class Repository<T>(
    private val remoteDataSource: T,
    private val localDataSource: T,
    private val authRepository: AuthRepository
) {
    protected val currentDataSource
        get() = if (authRepository.isSignedIn.value == true) remoteDataSource else localDataSource
}
