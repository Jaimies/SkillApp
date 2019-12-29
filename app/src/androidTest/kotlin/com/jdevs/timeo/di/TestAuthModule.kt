package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.FakeAuthRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestAuthModule {

    @Provides
    @Singleton
    fun provideUserManager(): AuthRepository = FakeAuthRepository

    @Provides
    @Singleton
    fun provideFakeUserManager(authRepository: AuthRepository) =
        authRepository as FakeAuthRepository
}
