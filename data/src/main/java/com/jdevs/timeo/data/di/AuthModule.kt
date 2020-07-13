package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.auth.DefaultAuthRepository
import com.jdevs.timeo.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface AuthModule {
    @Binds
    fun provideRepository(authRepository: DefaultAuthRepository): AuthRepository
}
