package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.DefaultAuthRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AuthModule {

    @Binds
    abstract fun provideAuthRepository(authRepository: DefaultAuthRepository): AuthRepository
}
