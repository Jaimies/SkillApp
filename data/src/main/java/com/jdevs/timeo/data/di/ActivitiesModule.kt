package com.jdevs.timeo.data.di

import com.jdevs.timeo.data.activities.ActivitiesLocalDataSource
import com.jdevs.timeo.data.activities.ActivitiesRemoteDataSource
import com.jdevs.timeo.data.activities.DefaultActivitiesRepository
import com.jdevs.timeo.data.activities.FirestoreActivitiesDataSource
import com.jdevs.timeo.data.activities.RoomActivitiesDataSource
import com.jdevs.timeo.domain.repository.ActivitiesRepository
import dagger.Binds
import dagger.Module

@Module
interface ActivitiesModule {

    @Binds
    fun provideRepository(repository: DefaultActivitiesRepository): ActivitiesRepository

    @Binds
    fun provideRemoteDataSource(source: FirestoreActivitiesDataSource): ActivitiesRemoteDataSource

    @Binds
    fun provideLocalDataSource(source: RoomActivitiesDataSource): ActivitiesLocalDataSource
}
