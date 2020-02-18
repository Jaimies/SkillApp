package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.records.DefaultRecordsRepository
import com.jdevs.timeo.data.records.FirestoreRecordsDataSource
import com.jdevs.timeo.data.records.RecordsLocalDataSource
import com.jdevs.timeo.data.records.RecordsRemoteDataSource
import com.jdevs.timeo.data.records.RoomRecordsDataSource
import com.jdevs.timeo.domain.repository.RecordsRepository
import dagger.Binds
import dagger.Module

@Module
interface RecordsModule {

    @Binds
    fun provideRepository(repository: DefaultRecordsRepository): RecordsRepository

    @Binds
    fun provideRemoteDataSource(source: FirestoreRecordsDataSource): RecordsRemoteDataSource

    @Binds
    fun provideLocalDataSource(source: RoomRecordsDataSource): RecordsLocalDataSource
}
