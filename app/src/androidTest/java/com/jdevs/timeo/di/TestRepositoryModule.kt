package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.FakeAndroidTestRepository
import com.jdevs.timeo.data.source.TimeoRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class TestRepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRepository(repository: FakeAndroidTestRepository): TimeoRepository
}