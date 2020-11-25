package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.records.RecordsRepositoryImpl
import com.jdevs.timeo.domain.repository.RecordsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface RecordsModule {
    @Binds
    fun provideRepository(repository: RecordsRepositoryImpl): RecordsRepository
}
