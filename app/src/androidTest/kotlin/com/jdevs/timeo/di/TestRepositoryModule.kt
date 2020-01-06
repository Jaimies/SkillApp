package com.jdevs.timeo.di

import com.jdevs.timeo.data.FakeRecordsRepository
import com.jdevs.timeo.data.records.RecordsRepository
import dagger.Binds
import dagger.Module

@Module
interface TestRepositoryModule {

    @Binds
    fun provideRepository(repository: FakeRecordsRepository): RecordsRepository
}
