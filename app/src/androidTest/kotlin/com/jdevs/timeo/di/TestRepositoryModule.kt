package com.jdevs.timeo.di

import com.jdevs.timeo.data.source.FakeRecordsRepository
import com.jdevs.timeo.data.source.RecordsRepository
import dagger.Binds
import dagger.Module

@Module
interface TestRepositoryModule {

    @Binds
    fun provideRepository(repository: FakeRecordsRepository): RecordsRepository
}
