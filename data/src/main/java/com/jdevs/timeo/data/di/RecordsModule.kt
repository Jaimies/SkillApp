package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.records.DefaultRecordsRepository
import com.jdevs.timeo.data.records.RecordsDataSource
import com.jdevs.timeo.data.records.RoomRecordsDataSource
import com.jdevs.timeo.domain.repository.RecordsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
interface RecordsModule {
    @Binds
    fun provideRepository(repository: DefaultRecordsRepository): RecordsRepository

    @Binds
    fun provideLocalDataSource(source: RoomRecordsDataSource): RecordsDataSource
}
