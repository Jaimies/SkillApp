package com.jdevs.timeo.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class BaseModule {
    @Provides
    @Singleton
    fun provideIoScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}
