package com.jdevs.timeo.di

import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.auth.DefaultAuthRepository
import dagger.Binds
import dagger.Module

@Module
interface AuthModule {

    @Binds
    fun provideAuthRepository(authRepository: DefaultAuthRepository): AuthRepository
}
