package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.auth.DefaultAuthRepository
import com.jdevs.timeo.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module

@Module
interface AuthModule {

    @Binds
    fun provideRepository(authRepository: DefaultAuthRepository): AuthRepository
}
