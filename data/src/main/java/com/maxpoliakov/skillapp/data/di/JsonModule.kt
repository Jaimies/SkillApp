package com.maxpoliakov.skillapp.data.di

import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
class JsonModule {
    @Provides
    @Reusable
    fun provideJson(): Json {
        return Json { encodeDefaults = true }
    }
}
