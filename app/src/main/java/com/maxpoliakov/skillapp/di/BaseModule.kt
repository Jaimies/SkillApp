package com.maxpoliakov.skillapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class BaseModule {
    @Provides
    @Singleton
    fun provideIoScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}
