package com.jdevs.timeo.di

import com.jdevs.timeo.data.FakeAuthRepository
import com.jdevs.timeo.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module

@Module
interface TestAuthModule {

    @Binds
    fun provideAuthRepository(authRepository: FakeAuthRepository): AuthRepository
}
