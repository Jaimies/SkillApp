package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.FakeAndroidTestRepository
import com.jdevs.timeo.data.source.TimeoRepository
import dagger.Binds
import dagger.Module

@Module
abstract class TestRepositoryModule {

    @Binds
    abstract fun provideRepository(repository: FakeAndroidTestRepository): TimeoRepository
}
