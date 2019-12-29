package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.FakeAuthRepository
import dagger.Binds
import dagger.Module

@Module
interface TestAuthModule {

    @Binds
    fun provideAuthRepository(authRepository: FakeAuthRepository): AuthRepository
}
