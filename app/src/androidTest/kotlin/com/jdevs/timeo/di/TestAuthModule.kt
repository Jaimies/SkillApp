package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.FakeAuthRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TestAuthModule {

    @Binds
    @Singleton
    abstract fun provideAuthRepository(authRepository: FakeAuthRepository): AuthRepository
}
